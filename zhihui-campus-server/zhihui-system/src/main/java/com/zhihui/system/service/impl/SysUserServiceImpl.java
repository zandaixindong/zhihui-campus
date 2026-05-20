package com.zhihui.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.exception.ServiceException;
import com.zhihui.common.core.utils.SecurityUtils;
import com.zhihui.system.entity.SysUser;
import com.zhihui.system.mapper.SysUserMapper;
import com.zhihui.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    private final SysUserMapper userMapper;

    @Override
    public PageResult<SysUser> selectUserList(SysUser user, int pageNum, int pageSize) {
        Page<SysUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(user.getUsername())) {
            wrapper.like(SysUser::getUsername, user.getUsername());
        }
        if (StringUtils.hasText(user.getRealName())) {
            wrapper.like(SysUser::getRealName, user.getRealName());
        }
        if (user.getUserType() != null) {
            wrapper.eq(SysUser::getUserType, user.getUserType());
        }
        if (user.getStatus() != null) {
            wrapper.eq(SysUser::getStatus, user.getStatus());
        }
        if (user.getCollegeId() != null) {
            wrapper.eq(SysUser::getCollegeId, user.getCollegeId());
        }

        wrapper.orderByDesc(SysUser::getCreateTime);

        Page<SysUser> result = userMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    public SysUser selectByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername, username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public SysUser selectUserById(Long userId) {
        return userMapper.selectById(userId);
    }

    @Override
    @Transactional
    public int insertUser(SysUser user) {
        // 检查用户名是否已存在
        SysUser existingUser = selectByUsername(user.getUsername());
        if (existingUser != null) {
            throw new ServiceException("用户名'" + user.getUsername() + "'已存在");
        }

        // 加密密码
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        }

        return userMapper.insert(user);
    }

    @Override
    @Transactional
    public int updateUser(SysUser user) {
        // 不能修改admin用户
        SysUser existingUser = userMapper.selectById(user.getId());
        if (existingUser != null && "admin".equals(existingUser.getUsername())) {
            throw new ServiceException("不允许修改超级管理员用户");
        }

        return userMapper.updateById(user);
    }

    @Override
    @Transactional
    public int deleteUserByIds(Long[] userIds) {
        // 不能删除admin用户
        List<Long> userIdList = Arrays.asList(userIds);
        for (Long userId : userIdList) {
            SysUser user = userMapper.selectById(userId);
            if (user != null && "admin".equals(user.getUsername())) {
                throw new ServiceException("不允许删除超级管理员用户");
            }
        }

        return userMapper.deleteByIds(userIdList);
    }

    @Override
    @Transactional
    public int resetPassword(Long userId, String password) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setPassword(SecurityUtils.encryptPassword(password));
        return userMapper.updateById(user);
    }

    @Override
    @Transactional
    public int updateStatus(Long userId, Integer status) {
        SysUser user = new SysUser();
        user.setId(userId);
        user.setStatus(status);
        return userMapper.updateById(user);
    }

    @Override
    public List<SysUser> selectUsersByRoleId(Long roleId) {
        return userMapper.selectUsersByRoleId(roleId);
    }
}
