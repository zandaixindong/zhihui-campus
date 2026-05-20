package com.zhihui.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.system.entity.SysMenu;
import com.zhihui.system.mapper.SysMenuMapper;
import com.zhihui.system.service.SysMenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    private final SysMenuMapper menuMapper;

    @Override
    public List<SysMenu> selectMenuAll() {
        return menuMapper.selectMenuAll();
    }

    @Override
    public List<SysMenu> selectMenusByUserId(Long userId) {
        // 管理员返回所有菜单
        if (userId == 1L) {
            return selectMenuAll();
        }
        return menuMapper.selectMenusByUserId(userId);
    }

    @Override
    public Set<String> selectPermsByUserId(Long userId) {
        List<SysMenu> menus = selectMenusByUserId(userId);
        Set<String> perms = new HashSet<>();
        for (SysMenu menu : menus) {
            if (StringUtils.hasText(menu.getPerms())) {
                perms.add(menu.getPerms());
            }
        }
        return perms;
    }

    @Override
    public List<SysMenu> buildMenuTree(List<SysMenu> menus) {
        // 按ID分组
        Map<Long, SysMenu> menuMap = menus.stream()
                .collect(Collectors.toMap(SysMenu::getId, m -> m));

        // 构建树
        List<SysMenu> tree = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menu.getParentId() == null || menu.getParentId() == 0L) {
                tree.add(menu);
            } else {
                SysMenu parent = menuMap.get(menu.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(menu);
                }
            }
        }

        return tree;
    }

    @Override
    @Transactional
    public int insertMenu(SysMenu menu) {
        return menuMapper.insert(menu);
    }

    @Override
    @Transactional
    public int updateMenu(SysMenu menu) {
        return menuMapper.updateById(menu);
    }

    @Override
    @Transactional
    public int deleteMenuById(Long menuId) {
        // 检查是否有子菜单
        LambdaQueryWrapper<SysMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysMenu::getParentId, menuId);
        if (menuMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("存在子菜单,不允许删除");
        }
        return menuMapper.deleteById(menuId);
    }

    @Override
    public List<SysMenu> selectMenusByRoleId(Long roleId) {
        return menuMapper.selectMenusByRoleId(roleId);
    }
}
