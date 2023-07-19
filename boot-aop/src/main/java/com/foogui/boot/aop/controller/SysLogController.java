package com.foogui.boot.aop.controller;

import com.foogui.boot.aop.annotation.MyAOP;
import com.foogui.common.AjaxResult;
import com.foogui.common.domain.DTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @PostMapping("/test")
    @MyAOP("foogui")
    public AjaxResult testAop(@RequestBody DTO dto){
        return AjaxResult.success("testAop测试成功");
    }

}
