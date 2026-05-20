package com.zhihui.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.student.entity.WorkStudy;
import com.zhihui.student.entity.WorkStudyApplication;

/**
 * 勤工助学服务接口
 */
public interface WorkStudyService extends IService<WorkStudy> {

    /**
     * 分页查询岗位列表
     */
    PageResult<WorkStudy> getPositionList(String positionType, Integer status, int pageNum, int pageSize);

    /**
     * 分页查询申请记录
     */
    PageResult<WorkStudyApplication> getApplicationList(Long studentId, Integer status, int pageNum, int pageSize);

    /**
     * 提交岗位申请
     */
    void submitApplication(WorkStudyApplication application);

    /**
     * 审批岗位申请
     */
    void approveApplication(Long applicationId, boolean approved, String remark);
}
