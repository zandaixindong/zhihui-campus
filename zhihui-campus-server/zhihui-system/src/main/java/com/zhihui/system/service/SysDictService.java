package com.zhihui.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.system.entity.SysDictData;
import com.zhihui.system.entity.SysDictType;

import java.util.List;

/**
 * 字典服务接口
 */
public interface SysDictService extends IService<SysDictType> {

    /**
     * 分页查询字典类型列表
     */
    PageResult<SysDictType> selectDictTypeList(SysDictType dictType, int pageNum, int pageSize);

    /**
     * 查询所有字典类型
     */
    List<SysDictType> selectDictTypeAll();

    /**
     * 根据字典类型查询字典数据
     */
    List<SysDictData> selectDictDataByType(String dictType);

    /**
     * 新增字典类型
     */
    int insertDictType(SysDictType dictType);

    /**
     * 修改字典类型
     */
    int updateDictType(SysDictType dictType);

    /**
     * 删除字典类型
     */
    int deleteDictTypeByIds(Long[] dictTypeIds);

    /**
     * 新增字典数据
     */
    int insertDictData(SysDictData dictData);

    /**
     * 修改字典数据
     */
    int updateDictData(SysDictData dictData);

    /**
     * 删除字典数据
     */
    int deleteDictDataByIds(Long[] dictDataIds);
}
