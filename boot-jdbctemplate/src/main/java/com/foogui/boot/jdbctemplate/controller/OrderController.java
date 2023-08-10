package com.foogui.boot.jdbctemplate.controller;

import com.foogui.boot.jdbctemplate.dao.OrderRepository;
import com.foogui.boot.jdbctemplate.domain.Order;
import com.foogui.common.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping("/getOrderById")
    public AjaxResult getOrderById(@RequestBody Order order){
        return AjaxResult.success(orderRepository.getOrderById(order.getId()));
    }

    @PostMapping("/getOrderByIdOnCallback")
    public AjaxResult getOrderByIdOnCallback(@RequestBody Order order){
        return AjaxResult.success(orderRepository.getOrderByIdOnCallback(order.getId()));
    }

    @PostMapping("/getOrderByIdOnTemplate")
    public AjaxResult getOrderByIdOnTemplate(@RequestBody Order order){
        return AjaxResult.success(orderRepository.getOrderByIdOnTemplate(order.getId()));
    }
}
