package com.zhihui.life.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zhihui.life.entity.RepairOrder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报修工单Mapper接口
 */
@Mapper
public interface RepairOrderMapper extends BaseMapper<RepairOrder> {
}
