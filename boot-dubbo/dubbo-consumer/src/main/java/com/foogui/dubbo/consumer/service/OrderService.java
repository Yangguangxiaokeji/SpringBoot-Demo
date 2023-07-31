package com.foogui.dubbo.consumer.service;

public interface OrderService {
    /**
     * 创建订单
     *
     * @param userId        用户id
     * @param commodityCode 商品编码
     * @param count         数量
     */
    Boolean createOrder(String userId, String commodityCode, Integer count);
}
