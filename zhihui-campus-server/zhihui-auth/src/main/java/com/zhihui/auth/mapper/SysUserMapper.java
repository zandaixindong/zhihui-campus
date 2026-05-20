package com.zhihui.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.auth.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户Mapper接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据用户名查询用户
     */
    SysUser selectByUsername(String username);

    /**
     * 根据手机号查询用户
     */
    SysUser selectByPhone(String phone);

    /**
     * 根据邮箱查询用户
     */
    SysUser selectByEmail(String email);
}
