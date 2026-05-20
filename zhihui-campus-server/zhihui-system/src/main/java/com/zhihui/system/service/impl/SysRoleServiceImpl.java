package com.zhihui.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.exception.ServiceException;
import com.zhihui.system.entity.SysRole;
import com.zhihui.system.mapper.SysRoleMapper;
import com.zhihui.system.service.SysRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 角色服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    private final SysRoleMapper roleMapper;

    @Override
    public PageResult<SysRole> selectRoleList(SysRole role, int pageNum, int pageSize) {
        Page<SysRole> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(role.getRoleName())) {
            wrapper.like(SysRole::getRoleName, role.getRoleName());
        }
        if (StringUtils.hasText(role.getRoleKey())) {
            wrapper.like(SysRole::getRoleKey, role.getRoleKey());
        }
        if (role.getStatus() != null) {
            wrapper.eq(SysRole::getStatus, role.getStatus());
        }

        wrapper.orderByAsc(SysRole::getSortOrder);

        Page<SysRole> result = roleMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    public List<SysRole> selectRoleAll() {
        return roleMapper.selectList(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getStatus, 1)
                .orderByAsc(SysRole::getSortOrder));
    }

    @Override
    public List<SysRole> selectRolesByUserId(Long userId) {
        return roleMapper.selectRolesByUserId(userId);
    }

    @Override
    public Set<String> selectRoleKeysByUserId(Long userId) {
        List<SysRole> roles = selectRolesByUserId(userId);
        Set<String> roleKeys = new HashSet<>();
        for (SysRole role : roles) {
            roleKeys.add(role.getRoleKey());
        }
        return roleKeys;
    }

    @Override
    @Transactional
    public int insertRole(SysRole role) {
        // 检查角色键是否已存在
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysRole::getRoleKey, role.getRoleKey());
        if (roleMapper.selectCount(wrapper) > 0) {
            throw new ServiceException("角色标识'" + role.getRoleKey() + "'已存在");
        }
        return roleMapper.insert(role);
    }

    @Override
    @Transactional
    public int updateRole(SysRole role) {
        // 不能修改admin角色
        SysRole existingRole = roleMapper.selectById(role.getId());
        if (existingRole != null && "admin".equals(existingRole.getRoleKey())) {
            throw new ServiceException("不允许修改超级管理员角色");
        }
        return roleMapper.updateById(role);
    }

    @Override
    @Transactional
    public int deleteRoleByIds(Long[] roleIds) {
        // 不能删除admin角色
        for (Long roleId : roleIds) {
            SysRole role = roleMapper.selectById(roleId);
            if (role != null && "admin".equals(role.getRoleKey())) {
                throw new ServiceException("不允许删除超级管理员角色");
            }
        }
        return roleMapper.deleteByIds(Arrays.asList(roleIds));
    }

    @Override
    @Transactional
    public int updateStatus(Long roleId, Integer status) {
        SysRole role = new SysRole();
        role.setId(roleId);
        role.setStatus(status);
        return roleMapper.updateById(role);
    }

    @Override
    @Transactional
    public void insertUserRole(Long roleId, Long[] userIds) {
        // TODO: 实现用户角色关联
    }
}
