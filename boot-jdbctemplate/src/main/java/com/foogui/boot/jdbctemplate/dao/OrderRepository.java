package com.foogui.boot.jdbctemplate.dao;


import com.foogui.boot.jdbctemplate.domain.Order;

public interface OrderRepository {
     Order getOrderById(Long orderId);
     Order getOrderByIdOnCallback(Long orderId);
     Order getOrderByIdOnTemplate(Long orderId);
}
