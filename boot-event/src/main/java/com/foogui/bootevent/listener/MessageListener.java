package com.foogui.bootevent.listener;

import com.foogui.bootevent.event.MessageEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MessageListener implements ApplicationListener<MessageEvent> {
    @Override
    public void onApplicationEvent(MessageEvent event) {
        System.out.println("当前线程名："+Thread.currentThread().getName());
        System.out.println("执行监听器的业务逻辑");
        System.out.println("用户注册成功，执行监听事件"+event.getSource());
    }
}
