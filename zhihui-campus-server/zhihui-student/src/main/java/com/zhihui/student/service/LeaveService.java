package com.zhihui.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.student.entity.LeaveApplication;

/**
 * 请假服务接口
 */
public interface LeaveService extends IService<LeaveApplication> {

    /**
     * 分页查询请假申请
     */
    PageResult<LeaveApplication> getLeaveList(Long studentId, Integer status, int pageNum, int pageSize);

    /**
     * 提交请假申请
     */
    void submitLeave(LeaveApplication leave);

    /**
     * 辅导员审批
     */
    void counselorApprove(Long leaveId, boolean approved, String remark);

    /**
     * 学院审批
     */
    void collegeApprove(Long leaveId, Long approverId, boolean approved, String remark);

    /**
     * 取消请假申请
     */
    void cancelLeave(Long leaveId);
}
