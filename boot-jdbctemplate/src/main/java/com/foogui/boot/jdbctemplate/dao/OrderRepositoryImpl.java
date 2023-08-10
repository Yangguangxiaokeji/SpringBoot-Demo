package com.foogui.boot.jdbctemplate.dao;

import com.foogui.boot.jdbctemplate.callback.CallbackJdbcTemplate;
import com.foogui.boot.jdbctemplate.callback.StatementCallback;
import com.foogui.boot.jdbctemplate.domain.Order;
import com.foogui.boot.jdbctemplate.template.OrderJdbcTemplate;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    private CallbackJdbcTemplate callbackJdbcTemplate;

    private OrderJdbcTemplate orderJdbcTemplate;

    // 构造器方式注入bean
    @Autowired
    public OrderRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // set方式注入
    @Autowired
    public void setOrderJdbcTemplate(OrderJdbcTemplate orderJdbcTemplate) {
        this.orderJdbcTemplate = orderJdbcTemplate;
    }

    /**
     * 方式一：jdbctemplate提供的查询方式
     *
     * @param orderId 订单id
     * @return {@link Order}
     */
    @Override
    public Order getOrderById(Long orderId) {

        return jdbcTemplate.queryForObject("select * from t_order where id=" + orderId, this::mapRowToOrder);
    }

    @Override
    public Order getOrderByIdOnCallback(Long orderId) {
        // 当然这里可以采用匿名内部类或lambda方式
        List<Object> objects = callbackJdbcTemplate.executeQuery("select * from t_order where id=" + orderId, new OrderStatementCallback());
        return objects!=null ? (Order) objects.get(0) : null;
    }

    static class OrderStatementCallback implements StatementCallback {
        @Override
        @SneakyThrows
        public List<Object> handleResultSet(ResultSet rs) throws SQLException {
            List<Object> orders = new ArrayList<>();
            while (rs.next()) {
                Class<?> resultClassType = Order.class;
                Object instance = resultClassType.newInstance();
                // 设置属性值
                Field[] fields = resultClassType.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    Object value = rs.getObject(i + 1);
                    fields[i].set(instance, value);
                }
                orders.add(instance);
            }
            return orders;
        }
    }

    @Override
    public Order getOrderByIdOnTemplate(Long orderId) {
        List<Order> orders = orderJdbcTemplate.executeQuery("select * from t_order where id=" + orderId, Order.class);
        return orders != null ? orders.get(0) : null;
    }

    /**
     * ResultSet的一行数据到Po的映射处理
     * 只需要给出一行的映射读取即可
     *
     * @param rs     rs
     * @param rowNum 行num
     * @return {@link Order}
     * @throws SQLException sqlexception异常
     */
    private Order mapRowToOrder(ResultSet rs, int rowNum) throws SQLException {
        return new Order(rs.getLong("id"), rs.getString("order_name"));
    }
}
