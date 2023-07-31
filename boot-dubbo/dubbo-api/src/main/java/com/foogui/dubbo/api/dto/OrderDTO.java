package com.foogui.dubbo.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 订单dto
 *
 * @author Foogui
 * @date 2023/07/31
 */
@Data
public class OrderDTO implements Serializable {

    private static final long serialVersionUID = -3789231312334910412L;
    private String orderNo;
    private String userId;
    private String commodityCode;
    private Integer orderCount;
    private Integer orderAmount;
}
