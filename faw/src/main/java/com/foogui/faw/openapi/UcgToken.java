package com.foogui.faw.openapi;

import lombok.Data;


@Data
public class UcgToken {

    private String access_token;

    private Long expire;

    private Long expiretime;

    private Long generatetime;
}
