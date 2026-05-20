package com.zhihui.system.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.system.entity.SysUser;
import com.zhihui.system.service.SysUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理", description = "用户增删改查接口")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController {

    private final SysUserService userService;

    /**
     * 分页查询用户列表
     */
    @Operation(summary = "分页查询用户列表")
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:user:list')")
    public R<PageResult<SysUser>> list(SysUser user,
                                       @RequestParam(defaultValue = "1") int pageNum,
                                       @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<SysUser> result = userService.selectUserList(user, pageNum, pageSize);
        return R.ok(result);
    }

    /**
     * 根据用户ID查询用户信息
     */
    @Operation(summary = "根据用户ID查询用户信息")
    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('system:user:query')")
    public R<SysUser> getInfo(@PathVariable Long userId) {
        return R.ok(userService.selectUserById(userId));
    }

    /**
     * 新增用户
     */
    @Operation(summary = "新增用户")
    @PostMapping
    @PreAuthorize("hasAuthority('system:user:add')")
    public R<Void> add(@RequestBody SysUser user) {
        return userService.insertUser(user) > 0 ? R.ok() : R.fail();
    }

    /**
     * 修改用户
     */
    @Operation(summary = "修改用户")
    @PutMapping
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R<Void> edit(@RequestBody SysUser user) {
        return userService.updateUser(user) > 0 ? R.ok() : R.fail();
    }

    /**
     * 删除用户
     */
    @Operation(summary = "删除用户")
    @DeleteMapping("/{userIds}")
    @PreAuthorize("hasAuthority('system:user:remove')")
    public R<Void> remove(@PathVariable Long[] userIds) {
        return userService.deleteUserByIds(userIds) > 0 ? R.ok() : R.fail();
    }

    /**
     * 重置密码
     */
    @Operation(summary = "重置密码")
    @PutMapping("/resetPwd")
    @PreAuthorize("hasAuthority('system:user:resetPwd')")
    public R<Void> resetPwd(@RequestParam Long userId, @RequestParam String password) {
        return userService.resetPassword(userId, password) > 0 ? R.ok() : R.fail();
    }

    /**
     * 修改用户状态
     */
    @Operation(summary = "修改用户状态")
    @PutMapping("/changeStatus")
    @PreAuthorize("hasAuthority('system:user:edit')")
    public R<Void> changeStatus(@RequestParam Long userId, @RequestParam Integer status) {
        return userService.updateStatus(userId, status) > 0 ? R.ok() : R.fail();
    }

    /**
     * 根据角色ID查询用户列表
     */
    @Operation(summary = "根据角色ID查询用户列表")
    @GetMapping("/role/{roleId}")
    public R<List<SysUser>> listByRoleId(@PathVariable Long roleId) {
        return R.ok(userService.selectUsersByRoleId(roleId));
    }
}
