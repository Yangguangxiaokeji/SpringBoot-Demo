package com.foogui.bootevent.controller;

import com.foogui.bootevent.po.User;
import com.foogui.bootevent.service.UserService;
import com.foogui.bootevent.template.event.RegisterMessageEvent;
import com.foogui.bootevent.template.publish.EventPublish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TemplateController {
    @Autowired
    private EventPublish eventPublish;
    @Autowired
    private UserService userService;

    @RequestMapping("/syncRegister")
    public String syncRegister(User user) {

        //注册成功过（类似于被观察者，做了某件事）
        userService.addUser(user);
        eventPublish.syncPublish(new RegisterMessageEvent("MsgId-1"));
        return "SUCCESS";
    }

    @RequestMapping("/asyncRegister")
    public String asyncRegister(User user) throws InterruptedException {

        //注册成功过（类似于被观察者，做了某件事）
        userService.addUser(user);
        eventPublish.asyncPublish(new RegisterMessageEvent("GiftId-1"));
        return "SUCCESS";
    }
}
