package com.foogui.bootevent.template.listener;

import com.foogui.bootevent.template.event.RegisterMessageEvent;
import org.springframework.stereotype.Component;
@Component
public class RegisterMessageListener implements IEventListener<RegisterMessageEvent>{
    @Override
    public boolean isNotified(RegisterMessageEvent event) {
        // 执行业务逻辑判断，返回true表示可以执行监听逻辑，也就是下面的handleEvent方法
        return true;
    }

    @Override
    public void handleEvent(RegisterMessageEvent event) {
        System.out.println("当前线程是"+Thread.currentThread().getName());
        System.out.println("事件父类的Source数据为:" + event.getSource());
        //模拟产生异常
        // int index =1/0;
        System.out.println("用户注册成功register，执行监听事件,获得的消息id为" + event.getMessageId());

    }

    @Override
    public void handleException(Throwable exception) {
        System.out.println("异常的消息为："+exception.getMessage());

    }
}
