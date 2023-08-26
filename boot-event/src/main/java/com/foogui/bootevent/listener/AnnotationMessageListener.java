package com.foogui.bootevent.listener;

import com.foogui.bootevent.event.MessageEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AnnotationMessageListener {
    @EventListener
    public void event(MessageEvent messageEvent){
        System.out.println("当前线程名："+Thread.currentThread().getName());
        System.out.println("执行事件的业务逻辑");
        messageEvent.sendMsg("富贵真可爱");
    }
}
