package com.foogui.boot.jdbctemplate.template;

import com.foogui.common.utils.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Slf4j
public abstract class AbstractJdbcTemplate {
    /**
     * 遗留问题：数据源Bean无法autowired注入
     */
    private DataSource dataSource;


    public final <T> List<T> executeQuery(String sql, Class<T> resultClassType) {
        dataSource= (DataSource) SpringBeanUtils.getBean("datasource-jdbc");
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return handleResultSet(resultSet, resultClassType);
        } catch (SQLException e) {
            log.error("Error executing query {}", e.getMessage());
        }
        return null;
    }

    public abstract <T> List<T> handleResultSet(ResultSet resultSet, Class<T> resultClassType);
}
