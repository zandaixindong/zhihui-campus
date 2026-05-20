package com.zhihui.edu.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhihui.edu.entity.Score;
import com.zhihui.edu.mapper.ScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 成绩服务
 */
@Service
@RequiredArgsConstructor
public class ScoreService {

    private final ScoreMapper scoreMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 分页查询成绩
     */
    public Page<Score> getScoreList(Long studentId, Long semesterId, int pageNum, int pageSize) {
        LambdaQueryWrapper<Score> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Score::getStudentId, studentId)
                .eq(Score::getStatus, 0)
                .orderByDesc(Score::getCreateTime);

        if (semesterId != null) {
            // 按学期筛选需要关联查询，此处简化处理
        }

        return scoreMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
    }

    /**
     * 获取GPA统计
     */
    public Map<String, Object> getGpaStats(Long studentId) {
        String cacheKey = "edu:gpa:" + studentId;
        Map<String, Object> cached = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return cached;
        }

        Map<String, Object> stats = scoreMapper.selectGpaStats(studentId);
        redisTemplate.opsForValue().set(cacheKey, stats, 1, TimeUnit.HOURS);
        return stats;
    }
}
