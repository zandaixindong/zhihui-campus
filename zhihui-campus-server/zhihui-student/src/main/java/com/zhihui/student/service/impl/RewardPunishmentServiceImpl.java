package com.zhihui.student.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.student.entity.RewardPunishment;
import com.zhihui.student.mapper.RewardPunishmentMapper;
import com.zhihui.student.service.RewardPunishmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 奖惩服务实现类
 */
@Service
@RequiredArgsConstructor
public class RewardPunishmentServiceImpl extends ServiceImpl<RewardPunishmentMapper, RewardPunishment>
        implements RewardPunishmentService {

    private final RewardPunishmentMapper recordMapper;

    @Override
    public PageResult<RewardPunishment> getRecordList(Long studentId, Integer type, int pageNum, int pageSize) {
        Page<RewardPunishment> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<RewardPunishment> wrapper = new LambdaQueryWrapper<>();

        if (studentId != null) {
            wrapper.eq(RewardPunishment::getStudentId, studentId);
        }
        if (type != null) {
            wrapper.eq(RewardPunishment::getType, type);
        }
        wrapper.orderByDesc(RewardPunishment::getEventDate);

        Page<RewardPunishment> result = recordMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void addRecord(RewardPunishment record) {
        record.setStatus(1); // 已发布
        record.setCreateTime(LocalDateTime.now());
        recordMapper.insert(record);
    }

    @Override
    @Transactional
    public void updateRecord(RewardPunishment record) {
        record.setUpdateTime(LocalDateTime.now());
        recordMapper.updateById(record);
    }

    @Override
    @Transactional
    public void deleteRecord(Long id) {
        recordMapper.deleteById(id);
    }
}
