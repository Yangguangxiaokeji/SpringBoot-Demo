package com.foogui.bootevent.controller;

import com.foogui.bootevent.config.ConfigurationPropertiesConfig;
import com.foogui.bootevent.event.MessageEvent;
import com.foogui.bootevent.observer.RegisterObserver;
import com.foogui.bootevent.po.User;
import com.foogui.bootevent.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@Slf4j
public class UserController implements ApplicationContextAware {

    @Value("${register.user.name}")
    private String username;

    @Autowired
    private UserService userService;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    //定义观察者列表
    private Collection<RegisterObserver> regObservers;

    @Autowired
    private ConfigurationPropertiesConfig configurationPropertiesConfig;

    /**
     * 方式一：异步
     *
     * @param user 用户
     * @return {@link String}
     */
    @RequestMapping("/register")
    public String register(User user) {
        //注册成功过（类似于被观察者，做了某件事）
        userService.addUser(user);
        //然后就开始通知各个观察者。
        for(RegisterObserver temp:regObservers){
            temp.sendMsg(username+"注冊成功");
        }
        log.info("操作者是{}",configurationPropertiesConfig.getOperator());
        log.info("map的数据是{}",configurationPropertiesConfig.getPoints());
        return "SUCCESS";
    }

    /**
     * 方式二：利用Spring
     *
     * @param user 用户
     * @return {@link String}
     */
    @RequestMapping("/springRegister")
    public String springRegister(User user) {
        //注册成功过（类似于被观察者，做了某件事）
        userService.addUser(user);

        applicationEventPublisher.publishEvent(new MessageEvent("注册成功"));
        return "SUCCESS";
    }
    //利用spring的ApplicationContextAware，初始化所有观察者
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Collection<RegisterObserver> values = applicationContext.getBeansOfType(RegisterObserver.class).values();
        this.regObservers=values;
    }





}
