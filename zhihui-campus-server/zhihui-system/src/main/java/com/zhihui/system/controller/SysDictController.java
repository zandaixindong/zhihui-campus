package com.zhihui.system.controller;

import com.zhihui.common.core.domain.PageResult;
import com.zhihui.common.core.domain.R;
import com.zhihui.system.entity.SysDictData;
import com.zhihui.system.entity.SysDictType;
import com.zhihui.system.service.SysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 字典管理控制器
 */
@Tag(name = "字典管理", description = "字典类型和字典数据管理接口")
@RestController
@RequestMapping("/system/dict")
@RequiredArgsConstructor
public class SysDictController {

    private final SysDictService dictService;

    // ==================== 字典类型 ====================

    /**
     * 分页查询字典类型列表
     */
    @Operation(summary = "分页查询字典类型列表")
    @GetMapping("/type/list")
    @PreAuthorize("hasAuthority('system:dict:list')")
    public R<PageResult<SysDictType>> typeList(SysDictType dictType,
                                               @RequestParam(defaultValue = "1") int pageNum,
                                               @RequestParam(defaultValue = "10") int pageSize) {
        PageResult<SysDictType> result = dictService.selectDictTypeList(dictType, pageNum, pageSize);
        return R.ok(result);
    }

    /**
     * 查询所有字典类型
     */
    @Operation(summary = "查询所有字典类型")
    @GetMapping("/type/all")
    public R<List<SysDictType>> typeAll() {
        return R.ok(dictService.selectDictTypeAll());
    }

    /**
     * 根据字典类型ID查询字典类型信息
     */
    @Operation(summary = "根据字典类型ID查询字典类型信息")
    @GetMapping("/type/{dictId}")
    @PreAuthorize("hasAuthority('system:dict:query')")
    public R<SysDictType> getTypeInfo(@PathVariable Long dictId) {
        return R.ok(dictService.getById(dictId));
    }

    /**
     * 新增字典类型
     */
    @Operation(summary = "新增字典类型")
    @PostMapping("/type")
    @PreAuthorize("hasAuthority('system:dict:add')")
    public R<Void> addType(@RequestBody SysDictType dictType) {
        return dictService.insertDictType(dictType) > 0 ? R.ok() : R.fail();
    }

    /**
     * 修改字典类型
     */
    @Operation(summary = "修改字典类型")
    @PutMapping("/type")
    @PreAuthorize("hasAuthority('system:dict:edit')")
    public R<Void> editType(@RequestBody SysDictType dictType) {
        return dictService.updateDictType(dictType) > 0 ? R.ok() : R.fail();
    }

    /**
     * 删除字典类型
     */
    @Operation(summary = "删除字典类型")
    @DeleteMapping("/type/{dictIds}")
    @PreAuthorize("hasAuthority('system:dict:remove')")
    public R<Void> removeType(@PathVariable Long[] dictIds) {
        return dictService.deleteDictTypeByIds(dictIds) > 0 ? R.ok() : R.fail();
    }

    // ==================== 字典数据 ====================

    /**
     * 根据字典类型查询字典数据
     */
    @Operation(summary = "根据字典类型查询字典数据")
    @GetMapping("/data/{dictType}")
    public R<List<SysDictData>> dataByType(@PathVariable String dictType) {
        return R.ok(dictService.selectDictDataByType(dictType));
    }

    /**
     * 根据字典数据ID查询字典数据信息
     */
    @Operation(summary = "根据字典数据ID查询字典数据信息")
    @GetMapping("/data/info/{dictCode}")
    @PreAuthorize("hasAuthority('system:dict:query')")
    public R<SysDictData> getDataInfo(@PathVariable Long dictCode) {
        // TODO: 查询字典数据
        return R.ok();
    }

    /**
     * 新增字典数据
     */
    @Operation(summary = "新增字典数据")
    @PostMapping("/data")
    @PreAuthorize("hasAuthority('system:dict:add')")
    public R<Void> addData(@RequestBody SysDictData dictData) {
        return dictService.insertDictData(dictData) > 0 ? R.ok() : R.fail();
    }

    /**
     * 修改字典数据
     */
    @Operation(summary = "修改字典数据")
    @PutMapping("/data")
    @PreAuthorize("hasAuthority('system:dict:edit')")
    public R<Void> editData(@RequestBody SysDictData dictData) {
        return dictService.updateDictData(dictData) > 0 ? R.ok() : R.fail();
    }

    /**
     * 删除字典数据
     */
    @Operation(summary = "删除字典数据")
    @DeleteMapping("/data/{dictCodes}")
    @PreAuthorize("hasAuthority('system:dict:remove')")
    public R<Void> removeData(@PathVariable Long[] dictCodes) {
        return dictService.deleteDictDataByIds(dictCodes) > 0 ? R.ok() : R.fail();
    }
}
