package com.foogui.boot.aop.controller;

import com.foogui.boot.aop.annotation.MyAOP;
import com.foogui.common.AjaxResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @PostMapping("/test")
    @MyAOP("foogui")
    public AjaxResult testAop(){
        return AjaxResult.success("testAop测试成功");
    }

}
