package com.foogui.bootevent.template.event;

import lombok.Data;

@Data
public class GiftSendEvent extends BaseEvent {

    private String giftId;

    public GiftSendEvent(String giftId) {
        super();
        this.giftId = giftId;
    }

}
