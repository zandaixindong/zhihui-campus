package com.zhihui.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.system.entity.SysUser;

import java.util.List;

/**
 * 用户服务接口
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 分页查询用户列表
     */
    PageResult<SysUser> selectUserList(SysUser user, int pageNum, int pageSize);

    /**
     * 根据用户名查询用户
     */
    SysUser selectByUsername(String username);

    /**
     * 根据用户ID查询用户信息（包含角色和权限）
     */
    SysUser selectUserById(Long userId);

    /**
     * 新增用户
     */
    int insertUser(SysUser user);

    /**
     * 修改用户
     */
    int updateUser(SysUser user);

    /**
     * 删除用户
     */
    int deleteUserByIds(Long[] userIds);

    /**
     * 重置密码
     */
    int resetPassword(Long userId, String password);

    /**
     * 修改用户状态
     */
    int updateStatus(Long userId, Integer status);

    /**
     * 根据角色ID查询用户列表
     */
    List<SysUser> selectUsersByRoleId(Long roleId);
}
