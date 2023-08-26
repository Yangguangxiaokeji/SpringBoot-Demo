package com.foogui.bootevent.service;

import com.foogui.bootevent.po.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    public void addUser(User user) {
        System.out.println("注册成功");
    }
}
