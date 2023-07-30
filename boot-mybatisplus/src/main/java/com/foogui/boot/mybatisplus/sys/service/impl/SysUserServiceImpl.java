package com.foogui.boot.mybatisplus.sys.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.foogui.boot.mybatisplus.sys.entity.SysUser;
import com.foogui.boot.mybatisplus.sys.mapper.SysUserMapper;
import com.foogui.boot.mybatisplus.sys.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 用户信息表 服务实现类
 * </p>
 *
 * @author Foogui
 * @since 2023-04-01
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    @Resource
    private SysUserMapper sysUserMapper;
    @DS("slave_1")
    @Override
    public Object getByCondition(Long userId) {
        return sysUserMapper.selectById(userId);
    }
}
