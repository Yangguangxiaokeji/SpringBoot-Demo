package com.foogui.bootevent.observer;

import org.springframework.stereotype.Component;

@Component
public class ObserverMobileImpl implements RegisterObserver {
    @Override
    public void sendMsg(String msg) {
        System.out.println("发送手机短信消息"+msg);
    }
}
