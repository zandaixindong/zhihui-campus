package com.zhihui.life.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.life.entity.RepairOrder;

/**
 * 报修服务接口
 */
public interface RepairService extends IService<RepairOrder> {

    /**
     * 分页查询报修工单
     */
    PageResult<RepairOrder> getRepairList(Long userId, Integer status, int pageNum, int pageSize);

    /**
     * 提交报修工单
     */
    void submitRepair(RepairOrder order);

    /**
     * 处理报修工单
     */
    void handleRepair(Long orderId, Long handlerId, String remark);

    /**
     * 完成报修工单
     */
    void completeRepair(Long orderId);

    /**
     * 取消报修工单
     */
    void cancelRepair(Long orderId);
}
