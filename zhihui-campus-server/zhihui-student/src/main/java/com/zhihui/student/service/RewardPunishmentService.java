package com.zhihui.student.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.student.entity.RewardPunishment;

/**
 * 奖惩服务接口
 */
public interface RewardPunishmentService extends IService<RewardPunishment> {

    /**
     * 分页查询奖惩记录
     */
    PageResult<RewardPunishment> getRecordList(Long studentId, Integer type, int pageNum, int pageSize);

    /**
     * 添加奖惩记录
     */
    void addRecord(RewardPunishment record);

    /**
     * 修改奖惩记录
     */
    void updateRecord(RewardPunishment record);

    /**
     * 删除奖惩记录
     */
    void deleteRecord(Long id);
}
