package com.foogui.bootevent.observer;

import org.springframework.stereotype.Component;

@Component
public class ObserverQQImpl implements RegisterObserver {
    @Override
    public void sendMsg(String msg) {
        System.out.println("发送QQ消息"+msg);
    }
}
