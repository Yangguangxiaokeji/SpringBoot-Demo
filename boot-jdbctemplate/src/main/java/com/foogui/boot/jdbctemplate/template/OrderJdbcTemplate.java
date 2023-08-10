package com.foogui.boot.jdbctemplate.template;

import lombok.SneakyThrows;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 子类实现抽象父类，实现模板方法中未实现的部分即可
 * 但是有一个问题就是子类会定义的非常多，于是就衍生出回调接口的方式代替模板方式
 *
 * @author Foogui
 * @date 2023/04/12
 */
@Repository
public class OrderJdbcTemplate extends AbstractJdbcTemplate {


    @Override
    @SneakyThrows
    public <T> List<T> handleResultSet(ResultSet rs, Class<T> resultClassType)  {
        List<T> list = new ArrayList<>();
        while (rs.next()) {
            T instance = resultClassType.newInstance();
            // 设置属性值
            Field[] fields = resultClassType.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);
                Object value = rs.getObject(i + 1);
                fields[i].set(instance, value);
            }
            list.add(instance);
        }
        return list;
    }
}
