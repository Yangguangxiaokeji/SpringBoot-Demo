package com.foogui.foo.generator.service;

import com.foogui.foo.generator.domain.Column;
import com.foogui.foo.generator.domain.DataBase;
import com.foogui.foo.generator.domain.GenDTO;
import com.foogui.foo.generator.domain.Table;
import com.foogui.foo.generator.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@Slf4j
public class GenServiceImpl implements GenService {


    /**
     * 生成代码（下载方式）
     *
     * @param table 表信息
     * @return 数据
     */
    @Override
    public byte[] doCreateCodeByJSON(Table table) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        doGeneratorCode(table,zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();

    }

    @Override
    public byte[] doCreateCodeByDDL(GenDTO dto) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        doGeneratorCode(prepareTableByDDL(dto),zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    @Override
    public byte[] doCreateCodeBatch(GenDTO dto) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : dto.getTableNames()) {
            doGeneratorCode(prepareTableByMetaInfo(tableName,dto), zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    private Table prepareTableByMetaInfo(String tableName, GenDTO dto) {
        JDBCUtils.getMetaTable(tableName,dto.getDataBase());
        return null;
    }


    /**
     * 根据Table信息生成byte[]并注入到zip中
     *
     * @param table 表
     * @param zip   邮政编码
     */
    private void  doGeneratorCode(Table table, ZipOutputStream zip) {

        VelocityUtils.initEngine();

        VelocityContext context = VelocityUtils.prepareContext(table);

        List<String> templates = VelocityUtils.getTemplateList();

        for (String template : templates) {
            Template tpl = Velocity.getTemplate(template, "UTF-8");
            StringWriter stringWriter = new StringWriter();
            tpl.merge(context, stringWriter);
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(stringWriter.toString(), zip, "UTF-8");
                IOUtils.closeQuietly(stringWriter);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }

    }

    /**
     * 准备表上下文
     *
     * @param dto
     */
    private Table prepareTableByDDL(GenDTO dto) {
        String ddl=dto.getDdl();

        Table table = new Table();
        // 去除sql中的飘号
        ddl = StringUtils.removeFly(ddl);

        String tableName = SqlParseUtils.getTableName(ddl);

        String projectName = dto.getProjectName();

        String className = StringUtils.removePrefixAndToCamel(tableName, dto.getPrefix());
        className = StringUtils.capitalizeFirstChar(className);
        String moduleName = StringUtils.removePrefixAndUnderline(tableName, dto.getPrefix());

        String primaryKey = SqlParseUtils.getPrimaryKey(ddl);

        // 处理Column信息
        Map<String, List<Column>> map = getColumns(ddl, primaryKey);
        table.setPk(map.get(CommonConstant.PK_COLUMN).get(0));
        table.setColumns(map.get(CommonConstant.NORMAL_COLUMN));

        table.setTableName(tableName);
        table.setProjectName(projectName);
        table.setClassName(className);
        table.setModuleName(moduleName);
        table.setFunctionName(dto.getBizName());
        table.setAuthor(dto.getAuthor());
        table.setPackageName(dto.getPackageName());
        table.setDataBase(DataBase.builder()
                .host(dto.getHost())
                .port(dto.getPort())
                .dbname(dto.getDbname())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .build()
        );
        return table;
    }

    /**
     * 获得列信息进行处理
     *
     * @param ddl        ddl
     * @param primaryKey 主键
     * @return {@link ArrayList}<{@link Column}>
     * @jsqlparser例外
     */
    private HashMap<String, List<Column>> getColumns(String ddl, String primaryKey) {
        ArrayList<Column> columnList = new ArrayList<>();
        ArrayList<Column> pkColumnList = new ArrayList<>();
        List<Column> columns = SqlParseUtils.getColumns(ddl);
        for (Column column : columns) {
            if (column.getColumnName().equals(primaryKey)) {
                pkColumnList.add(column);
            } else {
                columnList.add(column);
            }
        }
        HashMap<String, List<Column>> map = new HashMap<>();
        map.put(CommonConstant.PK_COLUMN, pkColumnList);
        map.put(CommonConstant.NORMAL_COLUMN, columnList);
        return map;
    }


}
