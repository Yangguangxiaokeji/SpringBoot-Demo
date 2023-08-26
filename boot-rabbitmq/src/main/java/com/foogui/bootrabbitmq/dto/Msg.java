package com.foogui.bootrabbitmq.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Msg implements Serializable {
    private static final long serialVersionUID = 2100914090841804996L;
    private String messageId;
    private String messageData;
    private String createTime;
}
