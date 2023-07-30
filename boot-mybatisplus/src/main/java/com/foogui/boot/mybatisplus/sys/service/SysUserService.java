package com.foogui.boot.mybatisplus.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.foogui.boot.mybatisplus.sys.entity.SysUser;

/**
 * <p>
 * 用户信息表 服务类
 * </p>
 *
 * @author Foogui
 * @since 2023-04-01
 */
public interface SysUserService extends IService<SysUser> {

    Object getByCondition(Long userId);
}
