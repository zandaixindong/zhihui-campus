package com.zhihui.life.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.life.entity.LostFound;

/**
 * 失物招领服务接口
 */
public interface LostFoundService extends IService<LostFound> {

    /**
     * 分页查询失物招领
     */
    PageResult<LostFound> getLostFoundList(Integer type, String category, Integer status,
                                           int pageNum, int pageSize);

    /**
     * 发布失物招领
     */
    void publishLostFound(LostFound lostFound);

    /**
     * 标记已解决
     */
    void resolveLostFound(Long id);
}
