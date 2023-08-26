package com.foogui.bootevent.template.event;

import org.springframework.context.ApplicationEvent;

public class BaseEvent extends ApplicationEvent {
    // 成员变量可以定义一些与业务相关的通用信息属性

    public BaseEvent(Object source) {
        super(source);
    }
    // 父类的Source中装"",实际的消息只存在子类中
    public BaseEvent() {
        this("");
    }
}
