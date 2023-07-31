package com.foogui.dubbo.api.api;

import com.foogui.dubbo.api.dto.OrderDTO;

public interface OrderDubboService {
    Boolean create(OrderDTO orderDTO);
}
