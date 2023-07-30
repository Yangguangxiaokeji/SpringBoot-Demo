package com.foogui.boot.mybatisplus.sys.controller;


import com.foogui.boot.mybatisplus.sys.entity.SysUser;
import com.foogui.boot.mybatisplus.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author Foogui
 * @since 2023-04-01
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController {
    @Autowired
    private SysUserService sysUserService;


    @PostMapping("/getByCondition")
    public Object query(@RequestBody SysUser sysUser) {
        return sysUserService.getByCondition(sysUser.getUserId());
    }

    @PostMapping("/insert")
    public Object insert(@RequestBody SysUser sysUser) {
        sysUserService.save(sysUser);
        return sysUser;
    }

    @PostMapping("/update")
    public Object update(@RequestBody SysUser sysUser) {
        sysUserService.updateById(sysUser);
        return sysUser;
    }

}
