package com.zhihui.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.system.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户Mapper接口
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 根据角色ID查询用户列表
     */
    List<SysUser> selectUsersByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据部门ID查询用户列表
     */
    List<SysUser> selectUsersByCollegeId(@Param("collegeId") Long collegeId);
}
