package com.zhihui.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.system.entity.SysDictData;
import com.zhihui.system.entity.SysDictType;
import com.zhihui.system.mapper.SysDictDataMapper;
import com.zhihui.system.mapper.SysDictTypeMapper;
import com.zhihui.system.service.SysDictService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 字典服务实现类
 */
@Service
@RequiredArgsConstructor
public class SysDictServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictService {

    private final SysDictTypeMapper dictTypeMapper;
    private final SysDictDataMapper dictDataMapper;

    @Override
    public PageResult<SysDictType> selectDictTypeList(SysDictType dictType, int pageNum, int pageSize) {
        Page<SysDictType> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(dictType.getDictName())) {
            wrapper.like(SysDictType::getDictName, dictType.getDictName());
        }
        if (StringUtils.hasText(dictType.getDictType())) {
            wrapper.like(SysDictType::getDictType, dictType.getDictType());
        }
        if (dictType.getStatus() != null) {
            wrapper.eq(SysDictType::getStatus, dictType.getStatus());
        }

        wrapper.orderByDesc(SysDictType::getCreateTime);

        Page<SysDictType> result = dictTypeMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    public List<SysDictType> selectDictTypeAll() {
        return dictTypeMapper.selectList(new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getStatus, 1));
    }

    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        return dictDataMapper.selectByDictType(dictType);
    }

    @Override
    @Transactional
    public int insertDictType(SysDictType dictType) {
        // 检查字典类型是否已存在
        LambdaQueryWrapper<SysDictType> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysDictType::getDictType, dictType.getDictType());
        if (dictTypeMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("字典类型'" + dictType.getDictType() + "'已存在");
        }
        return dictTypeMapper.insert(dictType);
    }

    @Override
    @Transactional
    public int updateDictType(SysDictType dictType) {
        return dictTypeMapper.updateById(dictType);
    }

    @Override
    @Transactional
    public int deleteDictTypeByIds(Long[] dictTypeIds) {
        return dictTypeMapper.deleteByIds(Arrays.asList(dictTypeIds));
    }

    @Override
    @Transactional
    public int insertDictData(SysDictData dictData) {
        return dictDataMapper.insert(dictData);
    }

    @Override
    @Transactional
    public int updateDictData(SysDictData dictData) {
        return dictDataMapper.updateById(dictData);
    }

    @Override
    @Transactional
    public int deleteDictDataByIds(Long[] dictDataIds) {
        return dictDataMapper.deleteByIds(Arrays.asList(dictDataIds));
    }
}
