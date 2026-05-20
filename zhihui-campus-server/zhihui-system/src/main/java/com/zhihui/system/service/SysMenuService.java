package com.zhihui.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.system.entity.SysMenu;

import java.util.List;
import java.util.Set;

/**
 * 菜单服务接口
 */
public interface SysMenuService extends IService<SysMenu> {

    /**
     * 查询所有菜单列表
     */
    List<SysMenu> selectMenuAll();

    /**
     * 根据用户ID查询菜单列表
     */
    List<SysMenu> selectMenusByUserId(Long userId);

    /**
     * 根据用户ID查询菜单权限集合
     */
    Set<String> selectPermsByUserId(Long userId);

    /**
     * 构建菜单树
     */
    List<SysMenu> buildMenuTree(List<SysMenu> menus);

    /**
     * 新增菜单
     */
    int insertMenu(SysMenu menu);

    /**
     * 修改菜单
     */
    int updateMenu(SysMenu menu);

    /**
     * 删除菜单
     */
    int deleteMenuById(Long menuId);

    /**
     * 根据角色ID查询菜单列表
     */
    List<SysMenu> selectMenusByRoleId(Long roleId);
}
