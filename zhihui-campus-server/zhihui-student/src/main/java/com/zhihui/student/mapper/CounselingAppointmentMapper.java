package com.zhihui.student.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.student.entity.CounselingAppointment;
import org.apache.ibatis.annotations.Mapper;

/**
 * 心理咨询预约Mapper接口
 */
@Mapper
public interface CounselingAppointmentMapper extends BaseMapper<CounselingAppointment> {
}
