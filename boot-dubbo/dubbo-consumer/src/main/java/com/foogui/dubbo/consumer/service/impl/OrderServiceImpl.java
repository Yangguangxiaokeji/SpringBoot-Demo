package com.foogui.dubbo.consumer.service.impl;

import com.foogui.dubbo.api.api.AccountDubboService;
import com.foogui.dubbo.api.dto.AccountDTO;
import com.foogui.dubbo.consumer.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @DubboReference(check = false)
    private AccountDubboService accountDubboService;

    @Override
    public Boolean createOrder(String userId, String commodityCode, Integer count) {

        log.info("Create Order successfully");

        AccountDTO dto = new AccountDTO();
        dto.setUserId(userId);
        // 根据commodityCode去商品中心查询该商品的单价，假设为999
        dto.setAmount(999*count);
        accountDubboService.reduce(dto);

        return Boolean.TRUE;
    }
}
