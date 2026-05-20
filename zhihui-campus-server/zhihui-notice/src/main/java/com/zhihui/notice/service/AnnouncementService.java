package com.zhihui.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.notice.entity.Announcement;

/**
 * 公告服务接口
 */
public interface AnnouncementService extends IService<Announcement> {

    /**
     * 分页查询公告列表
     */
    PageResult<Announcement> getAnnouncementList(Integer noticeType, Long collegeId, Integer status,
                                                  int pageNum, int pageSize);

    /**
     * 查询公告详情
     */
    Announcement getAnnouncementDetail(Long id);

    /**
     * 发布公告
     */
    void publishAnnouncement(Announcement announcement);

    /**
     * 修改公告
     */
    void updateAnnouncement(Announcement announcement);

    /**
     * 删除公告
     */
    void deleteAnnouncement(Long id);
}
