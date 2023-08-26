package com.foogui.bootevent.event;

import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {
    public MessageEvent(Object source) {
        super(source);
    }
    /**
     * 事件处理事项
     * @param msg
     */
    public void sendMsg(String msg)
    {
        System.out.println("用户注册成功，发送消息"+msg);
    }
}
