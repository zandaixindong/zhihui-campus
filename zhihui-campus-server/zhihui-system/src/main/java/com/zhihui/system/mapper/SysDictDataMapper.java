package com.zhihui.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.system.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 字典数据Mapper接口
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    /**
     * 根据字典类型查询字典数据
     */
    List<SysDictData> selectByDictType(@Param("dictType") String dictType);
}
