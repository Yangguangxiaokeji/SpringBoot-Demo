package com.foogui.foo.generator.domain;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;


/**
 * 表
 *
 * @author Foogui
 * @date 2023/05/11
 */
@Data
public class Table {

    private static final long serialVersionUID = 1623794836078801057L;


    /**
     * 表名称
     */
    @NotBlank(message = "表名称不能为空")
    private String tableName;

    /**
     * 实体类名称(首字母大写)
     */
    @NotBlank(message = "实体类名称不能为空")
    private String className;


    /**
     * 生成模块名
     */
    @NotBlank(message = "生成模块名不能为空")
    private String moduleName;

    /**
     * 生成作者
     */
    @NotBlank(message = "作者不能为空")
    private String author;


    /**
     * 主键信息
     */
    private Column pk;

    /**
     * 方法功能说明
     */
    private String functionName;


    /**
     * 表列信息
     */
    @Valid
    private List<Column> columns;

    /**
     * 项目名称
     */
    @Valid
    private String projectName;

    private String packageName;

    private DataBase dataBase;
}
