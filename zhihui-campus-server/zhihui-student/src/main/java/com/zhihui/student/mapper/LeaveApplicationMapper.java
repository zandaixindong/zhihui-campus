package com.zhihui.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.student.entity.LeaveApplication;
import org.apache.ibatis.annotations.Mapper;

/**
 * 请假申请Mapper接口
 */
@Mapper
public interface LeaveApplicationMapper extends BaseMapper<LeaveApplication> {
}
