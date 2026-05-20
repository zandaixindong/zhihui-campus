package com.zhihui.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.notice.entity.Announcement;
import com.zhihui.notice.mapper.AnnouncementMapper;
import com.zhihui.notice.service.AnnouncementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 公告服务实现类
 */
@Service
@RequiredArgsConstructor
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    @Override
    public PageResult<Announcement> getAnnouncementList(Integer noticeType, Long collegeId, Integer status,
                                                         int pageNum, int pageSize) {
        Page<Announcement> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Announcement> wrapper = new LambdaQueryWrapper<>();

        if (noticeType != null) {
            wrapper.eq(Announcement::getNoticeType, noticeType);
        }
        if (collegeId != null) {
            wrapper.eq(Announcement::getCollegeId, collegeId);
        }
        if (status != null) {
            wrapper.eq(Announcement::getStatus, status);
        }

        wrapper.orderByDesc(Announcement::getIsTop)
                .orderByDesc(Announcement::getPublishTime);

        Page<Announcement> result = announcementMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    public Announcement getAnnouncementDetail(Long id) {
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement != null) {
            // 增加浏览次数
            announcement.setViewCount(announcement.getViewCount() + 1);
            announcementMapper.updateById(announcement);
        }
        return announcement;
    }

    @Override
    @Transactional
    public void publishAnnouncement(Announcement announcement) {
        announcement.setStatus(1); // 已发布
        announcement.setPublishTime(LocalDateTime.now());
        announcement.setViewCount(0);
        announcement.setCreateTime(LocalDateTime.now());
        announcementMapper.insert(announcement);
    }

    @Override
    @Transactional
    public void updateAnnouncement(Announcement announcement) {
        announcement.setUpdateTime(LocalDateTime.now());
        announcementMapper.updateById(announcement);
    }

    @Override
    @Transactional
    public void deleteAnnouncement(Long id) {
        announcementMapper.deleteById(id);
    }
}
