package com.zhihui.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.student.entity.CounselingAppointment;
import com.zhihui.student.mapper.CounselingAppointmentMapper;
import com.zhihui.student.service.CounselingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 心理咨询服务实现类
 */
@Service
@RequiredArgsConstructor
public class CounselingServiceImpl extends ServiceImpl<CounselingAppointmentMapper, CounselingAppointment>
        implements CounselingService {

    private final CounselingAppointmentMapper appointmentMapper;

    @Override
    public PageResult<CounselingAppointment> getAppointmentList(Long studentId, Integer status, int pageNum, int pageSize) {
        Page<CounselingAppointment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<CounselingAppointment> wrapper = new LambdaQueryWrapper<>();

        if (studentId != null) {
            wrapper.eq(CounselingAppointment::getStudentId, studentId);
        }
        if (status != null) {
            wrapper.eq(CounselingAppointment::getStatus, status);
        }
        wrapper.orderByDesc(CounselingAppointment::getCreateTime);

        Page<CounselingAppointment> result = appointmentMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    public List<CounselingAppointment> getAvailableSlots(Long counselorId, String date) {
        LambdaQueryWrapper<CounselingAppointment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CounselingAppointment::getCounselorId, counselorId)
                .eq(CounselingAppointment::getAppointmentDate, LocalDate.parse(date))
                .in(CounselingAppointment::getStatus, 0, 1); // 待确认或已确认
        return appointmentMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public void submitAppointment(CounselingAppointment appointment) {
        // 检查时间段是否可用
        List<CounselingAppointment> existing = getAvailableSlots(
                appointment.getCounselorId(),
                appointment.getAppointmentDate().toString()
        );
        for (CounselingAppointment existingAppt : existing) {
            if (appointment.getAppointmentTime().equals(existingAppt.getAppointmentTime())) {
                throw new RuntimeException("该时间段已被预约");
            }
        }

        appointment.setStatus(0); // 待确认
        appointment.setCreateTime(LocalDateTime.now());
        appointmentMapper.insert(appointment);
    }

    @Override
    @Transactional
    public void confirmAppointment(Long appointmentId, String remark) {
        CounselingAppointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("预约不存在");
        }
        if (appointment.getStatus() != 0) {
            throw new RuntimeException("预约状态不正确");
        }

        appointment.setStatus(1); // 已确认
        appointment.setCounselorRemark(remark);
        appointment.setUpdateTime(LocalDateTime.now());
        appointmentMapper.updateById(appointment);
    }

    @Override
    @Transactional
    public void cancelAppointment(Long appointmentId) {
        CounselingAppointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("预约不存在");
        }
        if (appointment.getStatus() >= 2) {
            throw new RuntimeException("预约已取消或已完成");
        }

        appointment.setStatus(2); // 已取消
        appointment.setUpdateTime(LocalDateTime.now());
        appointmentMapper.updateById(appointment);
    }

    @Override
    @Transactional
    public void completeAppointment(Long appointmentId, String record) {
        CounselingAppointment appointment = appointmentMapper.selectById(appointmentId);
        if (appointment == null) {
            throw new RuntimeException("预约不存在");
        }
        if (appointment.getStatus() != 1) {
            throw new RuntimeException("预约状态不正确");
        }

        appointment.setStatus(3); // 已完成
        appointment.setCounselingRecord(record);
        appointment.setUpdateTime(LocalDateTime.now());
        appointmentMapper.updateById(appointment);
    }
}
