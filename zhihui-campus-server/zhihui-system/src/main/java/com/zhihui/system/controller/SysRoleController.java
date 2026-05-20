package com.zhihui.system.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.system.entity.SysRole;
import com.zhihui.system.service.SysRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 角色管理控制器
 */
@Tag(name = "角色管理", description = "角色增删改查接口")
@RestController
@RequestMapping("/system/role")
@RequiredArgsConstructor
public class SysRoleController {

    private final SysRoleService roleService;

    /**
     * 分页查询角色列表
     */
    @Operation(summary = "分页查询角色列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:role:list')")
    public R<PageResult<SysRole>> list(SysRole role,
                                       @RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<SysRole> result = roleService.selectRoleList(role, pageNum, pageSize);
        return R.ok(result);
    }

    /**
     * 查询所有角色列表
     */
    @Operation(summary = "查询所有角色列表")
    @GetMapping("/all")
    public R<List<SysRole>> all() {
        return R.ok(roleService.selectRoleAll());
    }

    /**
     * 根据角色ID查询角色信息
     */
    @Operation(summary = "根据角色ID查询角色信息")
    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('system:role:query')")
    public R<SysRole> getInfo(@PathVariable Long roleId) {
        return R.ok(roleService.getById(roleId));
    }

    /**
     * 新增角色
     */
    @Operation(summary = "新增角色")
    @PostMapping
    @PreAuthorize("hasAuthority('system:role:add')")
    public R<Void> add(@RequestBody SysRole role) {
        return roleService.insertRole(role) > 0 ? R.ok() : R.fail();
    }

    /**
     * 修改角色
     */
    @Operation(summary = "修改角色")
    @PutMapping
    @PreAuthorize("hasAuthority('system:role:edit')")
    public R<Void> edit(@RequestBody SysRole role) {
        return roleService.updateRole(role) > 0 ? R.ok() : R.fail();
    }

    /**
     * 删除角色
     */
    @Operation(summary = "删除角色")
    @DeleteMapping("/{roleIds}")
    @PreAuthorize("hasAuthority('system:role:remove')")
    public R<Void> remove(@PathVariable Long[] roleIds) {
        return roleService.deleteRoleByIds(roleIds) > 0 ? R.ok() : R.fail();
    }

    /**
     * 修改角色状态
     */
    @Operation(summary = "修改角色状态")
    @PutMapping("/changeStatus")
    @PreAuthorize("hasAuthority('system:role:edit')")
    public R<Void> changeStatus(@RequestParam Long roleId, @RequestParam Integer status) {
        return roleService.updateStatus(roleId, status) > 0 ? R.ok() : R.fail();
    }

    /**
     * 根据用户ID查询角色列表
     */
    @Operation(summary = "根据用户ID查询角色列表")
    @GetMapping("/user/{userId}")
    public R<List<SysRole>> listByUserId(@PathVariable Long userId) {
        return R.ok(roleService.selectRolesByUserId(userId));
    }

    /**
     * 根据用户ID查询角色权限集合
     */
    @Operation(summary = "根据用户ID查询角色权限集合")
    @GetMapping("/user/{userId}/keys")
    public R<Set<String>> listRoleKeysByUserId(@PathVariable Long userId) {
        return R.ok(roleService.selectRoleKeysByUserId(userId));
    }
}
