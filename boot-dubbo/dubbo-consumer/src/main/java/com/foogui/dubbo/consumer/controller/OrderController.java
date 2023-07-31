package com.foogui.dubbo.consumer.controller;

import com.foogui.dubbo.consumer.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("/create")
    public String order(){
        orderService.createOrder("foogui","12345",4);
        return "OK";
    }
}
