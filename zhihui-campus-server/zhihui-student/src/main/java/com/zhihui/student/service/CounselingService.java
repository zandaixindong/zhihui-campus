package com.zhihui.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.student.entity.CounselingAppointment;

import java.util.List;

/**
 * 心理咨询服务接口
 */
public interface CounselingService extends IService<CounselingAppointment> {

    /**
     * 分页查询预约记录
     */
    PageResult<CounselingAppointment> getAppointmentList(Long studentId, Integer status, int pageNum, int pageSize);

    /**
     * 获取可用咨询师时间段
     */
    List<CounselingAppointment> getAvailableSlots(Long counselorId, String date);

    /**
     * 提交预约申请
     */
    void submitAppointment(CounselingAppointment appointment);

    /**
     * 确认预约
     */
    void confirmAppointment(Long appointmentId, String remark);

    /**
     * 取消预约
     */
    void cancelAppointment(Long appointmentId);

    /**
     * 完成咨询
     */
    void completeAppointment(Long appointmentId, String record);
}
