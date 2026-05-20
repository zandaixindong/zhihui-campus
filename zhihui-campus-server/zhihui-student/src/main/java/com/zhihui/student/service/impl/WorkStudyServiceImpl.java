package com.zhihui.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.student.entity.WorkStudy;
import com.zhihui.student.entity.WorkStudyApplication;
import com.zhihui.student.mapper.WorkStudyApplicationMapper;
import com.zhihui.student.mapper.WorkStudyMapper;
import com.zhihui.student.service.WorkStudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 勤工助学服务实现类
 */
@Service
@RequiredArgsConstructor
public class WorkStudyServiceImpl extends ServiceImpl<WorkStudyMapper, WorkStudy> implements WorkStudyService {

    private final WorkStudyMapper workStudyMapper;
    private final WorkStudyApplicationMapper applicationMapper;

    @Override
    public PageResult<WorkStudy> getPositionList(String positionType, Integer status, int pageNum, int pageSize) {
        Page<WorkStudy> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<WorkStudy> wrapper = new LambdaQueryWrapper<>();

        if (StringUtils.hasText(positionType)) {
            wrapper.eq(WorkStudy::getPositionType, positionType);
        }
        if (status != null) {
            wrapper.eq(WorkStudy::getStatus, status);
        }
        wrapper.orderByDesc(WorkStudy::getCreateTime);

        Page<WorkStudy> result = workStudyMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    public PageResult<WorkStudyApplication> getApplicationList(Long studentId, Integer status, int pageNum, int pageSize) {
        Page<WorkStudyApplication> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<WorkStudyApplication> wrapper = new LambdaQueryWrapper<>();

        if (studentId != null) {
            wrapper.eq(WorkStudyApplication::getStudentId, studentId);
        }
        if (status != null) {
            wrapper.eq(WorkStudyApplication::getStatus, status);
        }
        wrapper.orderByDesc(WorkStudyApplication::getCreateTime);

        Page<WorkStudyApplication> result = applicationMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void submitApplication(WorkStudyApplication application) {
        // 检查岗位是否存在
        WorkStudy position = workStudyMapper.selectById(application.getPositionId());
        if (position == null) {
            throw new RuntimeException("岗位不存在");
        }
        if (position.getStatus() != 0) {
            throw new RuntimeException("岗位已停止招聘");
        }
        if (position.getHiredCount() >= position.getRecruitCount()) {
            throw new RuntimeException("岗位已招满");
        }

        application.setStatus(0); // 待审批
        application.setCreateTime(LocalDateTime.now());
        applicationMapper.insert(application);
    }

    @Override
    @Transactional
    public void approveApplication(Long applicationId, boolean approved, String remark) {
        WorkStudyApplication application = applicationMapper.selectById(applicationId);
        if (application == null) {
            throw new RuntimeException("申请不存在");
        }
        if (application.getStatus() != 0) {
            throw new RuntimeException("申请状态不正确");
        }

        application.setStatus(approved ? 1 : 2); // 1已通过 2已拒绝
        application.setApproveRemark(remark);
        application.setApproveTime(LocalDateTime.now());
        application.setUpdateTime(LocalDateTime.now());

        applicationMapper.updateById(application);

        // 如果通过，更新岗位已录用人数
        if (approved) {
            WorkStudy position = workStudyMapper.selectById(application.getPositionId());
            if (position != null) {
                position.setHiredCount(position.getHiredCount() + 1);
                workStudyMapper.updateById(position);
            }
        }
    }
}
