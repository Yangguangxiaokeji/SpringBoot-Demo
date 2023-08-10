package com.foogui.boot.jdbctemplate.callback;

import com.foogui.common.utils.SpringBeanUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * 采用回调接口的方式代替了模板方式,就可以采用函数式编程
 *
 * @author Foogui
 * @date 2023/04/12
 */
@Repository
@Slf4j
public class CallbackJdbcTemplate implements ApplicationListener<ContextRefreshedEvent> {


    private DataSource dataSource;
    public final List<Object> executeQuery(String sql, StatementCallback callback) {
        dataSource= SpringBeanUtils.getBean("datasource-jdbc", DataSource.class);
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            return callback.handleResultSet(resultSet);
        } catch (SQLException e) {
            log.error("Error executing query {}", e.getMessage());
        }
        return null;
    }



    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        this.dataSource=applicationContext.getBean("datasource-jdbc", DataSource.class);
    }
}