package com.zhihui.notice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.notice.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 消息Mapper接口
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 统计未读消息数量
     */
    int countUnread(@Param("userId") Long userId);
}
