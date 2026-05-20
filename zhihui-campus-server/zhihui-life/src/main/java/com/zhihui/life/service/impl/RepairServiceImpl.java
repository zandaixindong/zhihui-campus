package com.zhihui.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.life.entity.RepairOrder;
import com.zhihui.life.mapper.RepairOrderMapper;
import com.zhihui.life.service.RepairService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 报修服务实现类
 */
@Service
@RequiredArgsConstructor
public class RepairServiceImpl extends ServiceImpl<RepairOrderMapper, RepairOrder> implements RepairService {

    private final RepairOrderMapper repairOrderMapper;

    @Override
    public PageResult<RepairOrder> getRepairList(Long userId, Integer status, int pageNum, int pageSize) {
        Page<RepairOrder> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<RepairOrder> wrapper = new LambdaQueryWrapper<>();

        if (userId != null) {
            wrapper.eq(RepairOrder::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(RepairOrder::getStatus, status);
        }
        wrapper.orderByDesc(RepairOrder::getCreateTime);

        Page<RepairOrder> result = repairOrderMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void submitRepair(RepairOrder order) {
        // 生成工单号
        order.setOrderNo("RX" + System.currentTimeMillis());
        order.setStatus(0); // 待处理
        order.setCreateTime(LocalDateTime.now());

        repairOrderMapper.insert(order);
    }

    @Override
    @Transactional
    public void handleRepair(Long orderId, Long handlerId, String remark) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("工单不存在");
        }
        if (order.getStatus() != 0) {
            throw new RuntimeException("工单状态不正确");
        }

        order.setStatus(1); // 处理中
        order.setHandlerId(handlerId);
        order.setHandleRemark(remark);
        order.setHandleTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        repairOrderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void completeRepair(Long orderId) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("工单不存在");
        }
        if (order.getStatus() != 1) {
            throw new RuntimeException("工单状态不正确");
        }

        order.setStatus(2); // 已完成
        order.setCompleteTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());

        repairOrderMapper.updateById(order);
    }

    @Override
    @Transactional
    public void cancelRepair(Long orderId) {
        RepairOrder order = repairOrderMapper.selectById(orderId);
        if (order == null) {
            throw new RuntimeException("工单不存在");
        }
        if (order.getStatus() >= 2) {
            throw new RuntimeException("工单已完成或已关闭，无法取消");
        }

        order.setStatus(3); // 已关闭
        order.setUpdateTime(LocalDateTime.now());

        repairOrderMapper.updateById(order);
    }
}
