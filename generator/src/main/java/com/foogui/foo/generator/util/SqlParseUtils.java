package com.foogui.foo.generator.util;

import com.foogui.foo.generator.domain.Column;
import com.foogui.foo.generator.enums.TypeEnum;
import lombok.SneakyThrows;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SqlParseUtils {

    public static void main(String[] args) {
        String sql = "CREATE TABLE `t_material_capacity`  (\n" +
                "  `id` bigint NOT NULL AUTO_INCREMENT,\n" +
                "  `unique_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '数据行唯一键',\n" +
                "  `material_code` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物料编码',\n" +
                "  `material_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物料名称',\n" +
                "  `supplier_code` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '供应商编码',\n" +
                "  `supplier_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '供应商名称',\n" +
                "  `normal_capacity_day` int NOT NULL DEFAULT 0 COMMENT '标准产能（日）',\n" +
                "  `normal_capacity_month` int NOT NULL DEFAULT 0 COMMENT '标准产能（月）',\n" +
                "  `max_capacity_day` int NOT NULL DEFAULT 0 COMMENT '极限产能（日）',\n" +
                "  `max_capacity_month` int NOT NULL DEFAULT 0 COMMENT '极限产能（月）',\n" +
                "  `pass_rate` int NOT NULL DEFAULT 0 COMMENT '合格率',\n" +
                "  `production_period` decimal(5, 2) NOT NULL DEFAULT 0.00 COMMENT '生产周期 单位：天',\n" +
                "  `haul_cycle` decimal(5, 2) NOT NULL DEFAULT 0.00 COMMENT '运输周期 生产周期 单位：天',\n" +
                "  `urgent_haul_cycle` decimal(5, 2) NOT NULL DEFAULT 0.00 COMMENT '紧急运输周期 生产周期 单位：天',\n" +
                "  `raw_purchase_cycle` decimal(5, 2) NOT NULL DEFAULT 0.00 COMMENT '原材料采购周期 单位：周',\n" +
                "  `fundraiser_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '筹措员编码多人逗号(,)分隔',\n" +
                "  `fundraiser_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '筹措员名称多人逗号(,)分隔',\n" +
                "  `purchaser_code` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '采购员编码多人逗号(,)分隔',\n" +
                "  `purchaser_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '采购员名称多人逗号(,)分隔',\n" +
                "  `production_line` varchar(8) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '产线 0、单线 1、双线 2、共线  多选逗号(,)分隔',\n" +
                "  `shift` int NOT NULL DEFAULT 0 COMMENT '班制 1、单班 2、双班 3、三班',\n" +
                "  `shift_duration` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '生产时间（小时）每班次时间逗号(,)分隔',\n" +
                "  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',\n" +
                "  `create_user_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '创建者登录名',\n" +
                "  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',\n" +
                "  `update_user_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '更新者登录名',\n" +
                "  PRIMARY KEY (`id`) USING BTREE,\n" +
                "  UNIQUE INDEX `IDX_UNQ`(`material_code` ASC, `supplier_code` ASC) USING BTREE,\n" +
                "  INDEX `IDX_FUNDRAISER_CODE`(`fundraiser_code` ASC) USING BTREE\n" +
                ") ENGINE = InnoDB AUTO_INCREMENT = 11297 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '物料供应能力基础数据' ROW_FORMAT = DYNAMIC;";
        // // 检查sql合法不
        // System.out.println(checkSql(sql));
        // // 查询内容
        // System.out.println(getSelectItems(sql));
        // // 查询条件
        // System.out.println(getWhere(sql));
        // // 查询表和别名的对应关系
        // System.out.println(getTableRelationship(sql));
        sql = StringUtils.removeFly(sql);
        System.out.println(getTableName(sql));
        // System.out.println(getTableColumns(sql));
        System.out.println(getTableComment(sql));
        System.out.println(getColumns(sql));

        System.out.println(getPrimaryKey(sql));
    }

    @SneakyThrows
    public static SelectBody getSelectBody(String sql) {
        Select select = (Select) CCJSqlParserUtil.parse(sql);
        return select.getSelectBody();
    }

    @SneakyThrows
    public static Select getSelect(String sql) {
        return (Select) CCJSqlParserUtil.parse(sql);
    }

    @SneakyThrows
    public static Map<String, String> getTableRelationship(String sql) {
        PlainSelect plainSelect = (PlainSelect) getSelectBody(sql);
        // 获取表明集合
        List<String> tableList = getTableList(sql);
        // 获取别名集合
        ArrayList<String> aliasList = new ArrayList<>();
        Alias alias = plainSelect.getFromItem().getAlias();
        aliasList.add(alias != null ? alias.getName() : "");
        getJoins(sql).forEach(join -> {
            Alias temp = join.getRightItem().getAlias();
            aliasList.add(temp != null ? temp.getName() : "");
        });
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < tableList.size(); i++) {
            map.put(tableList.get(i), aliasList.get(i));
        }
        return map;
    }


    /**
     * 获取tables的表名
     *
     * @param sql
     * @return
     */
    @SneakyThrows
    public static List<String> getTableList(String sql) {
        Select select = getSelect(sql);
        TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
        return tablesNamesFinder.getTableList(select);
    }

    /**
     * 获取join层级
     *
     * @param sql
     * @return
     */
    @SneakyThrows
    public static List<Join> getJoins(String sql) {
        SelectBody selectBody = getSelectBody(sql);
        if (selectBody instanceof PlainSelect) {
            List<Join> joins = ((PlainSelect) selectBody).getJoins();
            // 这里可以进行解析joins foreach
            return joins;
        }
        return new ArrayList<Join>();
    }


    /**
     * 获取limit值
     *
     * @param sql
     * @return
     */
    @SneakyThrows
    public static Limit getLimit(String sql) {
        SelectBody selectBody = getSelectBody(sql);
        if (selectBody instanceof PlainSelect) {
            return ((PlainSelect) selectBody).getLimit();
        }
        return null;
    }

    @SneakyThrows
    public static Expression getWhere(String sql) {
        SelectBody selectBody = getSelectBody(sql);
        if (selectBody instanceof PlainSelect) {
            return ((PlainSelect) selectBody).getWhere();
        }
        return null;
    }


    /**
     * 获取查询内容
     *
     * @param sql
     * @return
     */
    @SneakyThrows
    public static List<SelectItem> getSelectItems(String sql) {
        SelectBody selectBody = getSelectBody(sql);
        if (selectBody instanceof PlainSelect) {
            List<SelectItem> selectItems = ((PlainSelect) selectBody).getSelectItems();
            return selectItems;
        }
        return null;
    }

    @SneakyThrows
    public static boolean checkSql(String sql) {
        try {
            CCJSqlParserUtil.parse(sql);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 得到创建的表名
     *
     * @param sql 创建表sql
     * @return {@link String}
     * @ jsqlparser异常
     */
    @SneakyThrows
    public static String getTableName(String sql) {
        CreateTable createTableStatement = getStatement(sql);
        return createTableStatement.getTable().getName();
    }

    /**
     * 得到字段名集合
     *
     * @param sql 创建表sql
     * @return {@link List}<{@link String}>
     * @ jsqlparser例外
     */
    @SneakyThrows
    public static List<String> getTableColumns(String sql) {
        CreateTable createTableStatement = getStatement(sql);

        String tableName = createTableStatement.getTable().getName();
        List<ColumnDefinition> columnDefinitions = createTableStatement.getColumnDefinitions();
        ArrayList<String> res = new ArrayList<>(columnDefinitions.size());
        for (ColumnDefinition columnDefinition : columnDefinitions) {
            String columnName = columnDefinition.getColumnName();
            res.add(columnName);
        }
        return res;
    }

    @SneakyThrows
    public static String getTableComment(String sql) {
        CreateTable createTableStatement = getStatement(sql);
        return createTableStatement.getTable().getSchemaName();
    }

    @SneakyThrows
    public static List<Column> getColumns(String sql) {

        CreateTable createTableStatement = getStatement(sql);
        List<ColumnDefinition> definitions = createTableStatement.getColumnDefinitions();
        List<Column> columns = new ArrayList<Column>();
        for (ColumnDefinition definition : definitions) {
            Column column = new Column();
            column.setColumnName(definition.getColumnName());
            String columnType = definition.getColDataType().getDataType();
            column.setColumnType(columnType);
            List<String> columnSpecs = definition.getColumnSpecs();
            for (int i = 0; i < columnSpecs.size(); i++) {
                if ("COMMENT".equals(columnSpecs.get(i))) {
                    String columnComment = columnSpecs.get(i + 1).replace("'", "");
                    column.setColumnComment(columnComment);
                }
            }
            column.setJavaField(StringUtils.underlineToCamel(definition.getColumnName()));
            // 处理javaType，jdbcType与columnType的映射关系
            column.setJavaType(TypeEnum.getJavaType(columnType));
            column.setJdbcType(TypeEnum.getJdbcType(columnType));
            columns.add(column);
        }
        return columns;
    }

    @SneakyThrows
    private static CreateTable getStatement(String sql) {
        String ddl = sql;
        if (sql.contains("CREATE TABLE")) {
            String middle = extractDDL(sql);
            ddl = removeIndexInSql(middle);
        }
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        return (CreateTable) parserManager.parse(new StringReader(ddl));

    }

    @SneakyThrows
    private static CreateTable getTableStatement(String sql) {

        String middle = extractDDL(sql);
        String ddl = removeIndexInSql(middle);
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        CreateTable createTableStatement = (CreateTable) parserManager.parse(new StringReader(ddl));
        return createTableStatement;
    }

    @SneakyThrows
    private static String extractDDL(String sql) {
        String middle = StringUtils.substringBetween(sql, "CREATE TABLE", "DYNAMIC;");
        return "CREATE TABLE" + middle + "DYNAMIC;";
    }

    /**
     * 在sql中删除索引相关的String，只考虑了UNIQUE和NORMAL索引
     *
     * @param sql sql
     */
    @SneakyThrows
    private static String removeIndexInSql(String sql) {
        // todo: remove
        String head;
        String tail;
        String result = null;
        if (sql.contains("UNIQUE INDEX")) {
            head = StringUtils.substringBetween(sql, "CREATE TABLE", "UNIQUE INDEX");
            tail = StringUtils.substringBetween(sql, "ENGINE", "DYNAMIC;");
            String substring = StringUtils.substringBeforeLast(head, ",");
            result = "CREATE TABLE" + substring + ") ENGINE" + tail + "DYNAMIC;";
        } else if (sql.contains("NORMAL INDEX")) {
            head = StringUtils.substringBetween(sql, "CREATE TABLE", "NORMAL INDEX");
            tail = StringUtils.substringBetween(sql, "ENGINE", "DYNAMIC;");
            String substring = StringUtils.substringBeforeLast(head, ",");
            result = "CREATE TABLE" + substring + ") ENGINE" + tail + "DYNAMIC;";
        }
        return result;
    }

    @SneakyThrows
    public static String getPrimaryKey(String sql) {
        String primaryKey = null;
        CreateTable createTable = getStatement(sql);
        List<Index> indexes = createTable.getIndexes();
        for (Index index : indexes) {
            if (index.getType().equals("PRIMARY KEY")) {
                List<Index.ColumnParams> columns = index.getColumns();
                for (Index.ColumnParams column : columns) {
                    primaryKey = column.getColumnName();
                }
            }
        }
        return primaryKey;
    }


}
