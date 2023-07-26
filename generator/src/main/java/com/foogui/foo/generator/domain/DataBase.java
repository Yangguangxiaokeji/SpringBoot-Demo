package com.foogui.foo.generator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 数据库信息
 *
 * @author Foogui
 * @date 2023/07/22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataBase {
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
}
