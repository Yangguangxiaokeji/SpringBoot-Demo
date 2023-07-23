package com.foogui.foo.generator.util;

import com.foogui.foo.generator.domain.DataBase;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class JDBCUtils {
    public static void main(String[] args) {
        DataBase dataBase = new DataBase();
        dataBase.setHost("localhost");
        dataBase.setPort("3308");
        dataBase.setDbname("otd");
        dataBase.setUsername("root");
        dataBase.setPassword("root");
        JDBCUtils.getMetaTable("t_purchase_relation", dataBase);
    }

    @SneakyThrows
    public static MetaTable getMetaTable(String tableName, DataBase dataBase) {
        MetaTable metaTable = new MetaTable();
        ArrayList<MetaColumn> metaColumns = new ArrayList<>();
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn = null;
        ResultSet rsTable = null;
        ResultSet rsColumn = null;
        String tableSchema = null;
        String primaryKey = null;
        try {
            // 创建MySQL连接
            String url = String.format("jdbc:mysql://%s:%s/%s", dataBase.getHost(), dataBase.getPort(), dataBase.getDbname());
            String user = dataBase.getUsername();
            String password = dataBase.getPassword();
            conn = DriverManager.getConnection(url, user, password);
            conn = DriverManager.getConnection(url, user, password);

            // 获取数据库的元数据信息
            DatabaseMetaData databaseMetaData = conn.getMetaData();
            tableSchema = conn.getCatalog();
            // 获取表的元数据
            metaTable.setTableName(tableName);
            rsTable = databaseMetaData.getTables(conn.getCatalog(), null, null, new String[]{"TABLE", "VIEW", ""});
            if (rsTable.next()) {
                String sql = "SELECT TABLE_COMMENT FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + tableSchema + "' AND TABLE_NAME = '" + tableName + "'";
                ResultSet tableRs = conn.createStatement().executeQuery(sql);
                String tableComment = null;
                while (tableRs.next()) {
                    tableComment = tableRs.getString("TABLE_COMMENT");
                }
                metaTable.setTableComment(tableComment);
            }
            // 获取主键名
            String sql0 = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + tableSchema + "' AND TABLE_NAME = '" + tableName + "' AND COLUMN_KEY = 'PRI'";
            ResultSet rs2 = conn.createStatement().executeQuery(sql0);
            while (rs2.next()) {
                primaryKey=rs2.getString(1);
            }

            // 获取字段的元数据
            rsColumn = databaseMetaData.getColumns(tableSchema, null, tableName, null);
            while (rsColumn.next()) {
                MetaColumn metaColumn = new MetaColumn();
                String columnName = rsColumn.getString("COLUMN_NAME");
                metaColumn.setColumnName(columnName);
                metaColumn.setDataType(rsColumn.getString("TYPE_NAME"));
                metaColumn.setColumnSize(rsColumn.getInt("COLUMN_SIZE"));
                // 必须要从INFORMATION_SCHEMA才能查到字段注释，REMARKS拿不到
                String sql = "SELECT COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + tableSchema + "' AND TABLE_NAME = '" + tableName + "' AND COLUMN_NAME = '" + columnName + "'";
                ResultSet rs = conn.createStatement().executeQuery(sql);
                // 获取字段注释
                while (rs.next()) {
                    metaColumn.setColumnComment(rs.getString("COLUMN_COMMENT"));
                }
                if (columnName.equals(primaryKey)){
                    metaColumn.setIsPk(1);
                }


                metaColumns.add(metaColumn);
            }
            metaTable.setColumns(metaColumns);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (rsTable != null) {
                    rsTable.close();
                }
                if (rsColumn != null) {
                    rsColumn.close();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return metaTable;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MetaTable {
        private String tableName;
        private String tableComment;
        private List<MetaColumn> columns;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private static class MetaColumn {
        private String columnName;
        private String dataType;
        private int columnSize;
        private String columnComment;
        private int isPk;
    }

}
