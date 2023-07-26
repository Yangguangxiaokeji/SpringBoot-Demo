package com.foogui.foo.generator.domain;

import lombok.Data;

import java.util.List;

@Data
public class GenDTO {

    /**
     * 项目名称(不传默认为"gen-code")
     */
    private String projectName = "gen-code";

    /**
     * 表前缀（可选），按需添加,默认t_
     */
    private String prefix = "t_";

    /**
     * 业务名称(不传默认为"XX"),建议添加
     */
    private String bizName = "XX";

    /**
     * 包路径，建议添加
     */
    private String packageName = "com.foogui";

    /**
     * 作者
     */
    private String author = "FooGui";

    /**
     * 数据库host
     */
    private String host;
    /**
     * 端口
     */
    private String port;
    /**
     * 数据库名
     */
    private String dbname;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 表名集合
     */
    private List<String> tableNames;

    private String ddl;

    public DataBase getDataBase() {
        return DataBase.builder()
                .host(host)
                .port(port)
                .dbname(dbname)
                .username(username)
                .password(password).build();
    }

}
