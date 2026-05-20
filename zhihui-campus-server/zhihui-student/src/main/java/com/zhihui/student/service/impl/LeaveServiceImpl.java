package com.zhihui.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.student.entity.LeaveApplication;
import com.zhihui.student.mapper.LeaveApplicationMapper;
import com.zhihui.student.service.LeaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 请假服务实现类
 */
@Service
@RequiredArgsConstructor
public class LeaveServiceImpl extends ServiceImpl<LeaveApplicationMapper, LeaveApplication> implements LeaveService {

    private final LeaveApplicationMapper leaveMapper;

    @Override
    public PageResult<LeaveApplication> getLeaveList(Long studentId, Integer status, int pageNum, int pageSize) {
        Page<LeaveApplication> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<LeaveApplication> wrapper = new LambdaQueryWrapper<>();

        if (studentId != null) {
            wrapper.eq(LeaveApplication::getStudentId, studentId);
        }
        if (status != null) {
            wrapper.eq(LeaveApplication::getStatus, status);
        }
        wrapper.orderByDesc(LeaveApplication::getCreateTime);

        Page<LeaveApplication> result = leaveMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void submitLeave(LeaveApplication leave) {
        leave.setStatus(0); // 待审批
        leave.setCreateTime(LocalDateTime.now());
        leaveMapper.insert(leave);
    }

    @Override
    @Transactional
    public void counselorApprove(Long leaveId, boolean approved, String remark) {
        LeaveApplication leave = leaveMapper.selectById(leaveId);
        if (leave == null) {
            throw new RuntimeException("请假申请不存在");
        }
        if (leave.getStatus() != 0) {
            throw new RuntimeException("申请状态不正确");
        }

        leave.setStatus(approved ? 1 : 2); // 1辅导员通过 2辅导员拒绝
        leave.setCounselorRemark(remark);
        leave.setCounselorApproveTime(LocalDateTime.now());
        leave.setUpdateTime(LocalDateTime.now());

        leaveMapper.updateById(leave);
    }

    @Override
    @Transactional
    public void collegeApprove(Long leaveId, Long approverId, boolean approved, String remark) {
        LeaveApplication leave = leaveMapper.selectById(leaveId);
        if (leave == null) {
            throw new RuntimeException("请假申请不存在");
        }
        if (leave.getStatus() != 1) {
            throw new RuntimeException("申请状态不正确，需要辅导员先审批");
        }

        leave.setStatus(approved ? 3 : 4); // 3学院通过 4学院拒绝
        leave.setCollegeApproverId(approverId);
        leave.setCollegeRemark(remark);
        leave.setCollegeApproveTime(LocalDateTime.now());
        leave.setUpdateTime(LocalDateTime.now());

        leaveMapper.updateById(leave);
    }

    @Override
    @Transactional
    public void cancelLeave(Long leaveId) {
        LeaveApplication leave = leaveMapper.selectById(leaveId);
        if (leave == null) {
            throw new RuntimeException("请假申请不存在");
        }
        if (leave.getStatus() >= 1) {
            throw new RuntimeException("申请已审批，无法取消");
        }

        leaveMapper.deleteById(leaveId);
    }
}
