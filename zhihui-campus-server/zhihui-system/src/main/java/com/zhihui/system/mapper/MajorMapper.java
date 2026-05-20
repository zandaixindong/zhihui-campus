package com.zhihui.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.system.entity.Major;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 专业Mapper接口
 */
@Mapper
public interface MajorMapper extends BaseMapper<Major> {

    /**
     * 根据学院ID查询专业列表
     */
    List<Major> selectByCollegeId(@Param("collegeId") Long collegeId);
}
