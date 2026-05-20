package com.zhihui.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.life.entity.LostFound;
import com.zhihui.life.mapper.LostFoundMapper;
import com.zhihui.life.service.LostFoundService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * 失物招领服务实现类
 */
@Service
@RequiredArgsConstructor
public class LostFoundServiceImpl extends ServiceImpl<LostFoundMapper, LostFound> implements LostFoundService {

    private final LostFoundMapper lostFoundMapper;

    @Override
    public PageResult<LostFound> getLostFoundList(Integer type, String category, Integer status,
                                                   int pageNum, int pageSize) {
        Page<LostFound> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<LostFound> wrapper = new LambdaQueryWrapper<>();

        if (type != null) {
            wrapper.eq(LostFound::getType, type);
        }
        if (StringUtils.hasText(category)) {
            wrapper.eq(LostFound::getCategory, category);
        }
        if (status != null) {
            wrapper.eq(LostFound::getStatus, status);
        }
        wrapper.orderByDesc(LostFound::getCreateTime);

        Page<LostFound> result = lostFoundMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    @Transactional
    public void publishLostFound(LostFound lostFound) {
        lostFound.setStatus(0); // 进行中
        lostFound.setCreateTime(LocalDateTime.now());
        lostFoundMapper.insert(lostFound);
    }

    @Override
    @Transactional
    public void resolveLostFound(Long id) {
        LostFound lostFound = lostFoundMapper.selectById(id);
        if (lostFound == null) {
            throw new RuntimeException("记录不存在");
        }

        lostFound.setStatus(1); // 已解决
        lostFound.setUpdateTime(LocalDateTime.now());
        lostFoundMapper.updateById(lostFound);
    }
}
