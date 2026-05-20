package com.zhihui.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.system.entity.SysRole;

import java.util.List;
import java.util.Set;

/**
 * 角色服务接口
 */
public interface SysRoleService extends IService<SysRole> {

    /**
     * 分页查询角色列表
     */
    PageResult<SysRole> selectRoleList(SysRole role, int pageNum, int pageSize);

    /**
     * 查询所有角色列表
     */
    List<SysRole> selectRoleAll();

    /**
     * 根据用户ID查询角色列表
     */
    List<SysRole> selectRolesByUserId(Long userId);

    /**
     * 根据用户ID查询角色权限集合
     */
    Set<String> selectRoleKeysByUserId(Long userId);

    /**
     * 新增角色
     */
    int insertRole(SysRole role);

    /**
     * 修改角色
     */
    int updateRole(SysRole role);

    /**
     * 删除角色
     */
    int deleteRoleByIds(Long[] roleIds);

    /**
     * 修改角色状态
     */
    int updateStatus(Long roleId, Integer status);

    /**
     * 分配用户角色
     */
    void insertUserRole(Long roleId, Long[] userIds);
}
