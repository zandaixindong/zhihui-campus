package com.zhihui.system.controller;

import com.zhihui.common.core.domain.R;
import com.zhihui.system.entity.SysMenu;
import com.zhihui.system.service.SysMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理控制器
 */
@Tag(name = "菜单管理", description = "菜单增删改查接口")
@RestController
@RequestMapping("/system/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService menuService;

    /**
     * 查询所有菜单列表
     */
    @Operation(summary = "查询所有菜单列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:menu:list')")
    public R<List<SysMenu>> list() {
        List<SysMenu> menus = menuService.selectMenuAll();
        return R.ok(menus);
    }

    /**
     * 查询菜单树
     */
    @Operation(summary = "查询菜单树")
    @GetMapping("/tree")
    public R<List<SysMenu>> tree() {
        List<SysMenu> menus = menuService.selectMenuAll();
        return R.ok(menuService.buildMenuTree(menus));
    }

    /**
     * 根据菜单ID查询菜单信息
     */
    @Operation(summary = "根据菜单ID查询菜单信息")
    @GetMapping("/{menuId}")
    @PreAuthorize("hasAuthority('system:menu:query')")
    public R<SysMenu> getInfo(@PathVariable Long menuId) {
        return R.ok(menuService.getById(menuId));
    }

    /**
     * 新增菜单
     */
    @Operation(summary = "新增菜单")
    @PostMapping
    @PreAuthorize("hasAuthority('system:menu:add')")
    public R<Void> add(@RequestBody SysMenu menu) {
        return menuService.insertMenu(menu) > 0 ? R.ok() : R.fail();
    }

    /**
     * 修改菜单
     */
    @Operation(summary = "修改菜单")
    @PutMapping
    @PreAuthorize("hasAuthority('system:menu:edit')")
    public R<Void> edit(@RequestBody SysMenu menu) {
        return menuService.updateMenu(menu) > 0 ? R.ok() : R.fail();
    }

    /**
     * 删除菜单
     */
    @Operation(summary = "删除菜单")
    @DeleteMapping("/{menuId}")
    @PreAuthorize("hasAuthority('system:menu:remove')")
    public R<Void> remove(@PathVariable Long menuId) {
        return menuService.deleteMenuById(menuId) > 0 ? R.ok() : R.fail();
    }

    /**
     * 根据用户ID查询菜单列表
     */
    @Operation(summary = "根据用户ID查询菜单列表")
    @GetMapping("/user/{userId}")
    public R<List<SysMenu>> listByUserId(@PathVariable Long userId) {
        return R.ok(menuService.selectMenusByUserId(userId));
    }

    /**
     * 根据用户ID查询菜单权限集合
     */
    @Operation(summary = "根据用户ID查询菜单权限集合")
    @GetMapping("/user/{userId}/perms")
    public R<Set<String>> listPermsByUserId(@PathVariable Long userId) {
        return R.ok(menuService.selectPermsByUserId(userId));
    }
}
