package com.foogui.dubbo.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 账户dto
 *
 * @author Foogui
 * @date 2023/07/31
 */
@Data
public class AccountDTO implements Serializable {
    private static final long serialVersionUID = -3224661220666063181L;
    private Integer id;
    private String userId;
    private Integer amount;
}