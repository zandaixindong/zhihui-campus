package com.zhihui.life.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.life.entity.Venue;
import com.zhihui.life.entity.VenueBooking;

import java.time.LocalDate;
import java.util.List;

/**
 * 场地服务接口
 */
public interface VenueService extends IService<Venue> {

    /**
     * 查询场地列表
     */
    List<Venue> getVenueList(String venueType);

    /**
     * 分页查询预约记录
     */
    PageResult<VenueBooking> getBookingList(Long userId, Integer status, int pageNum, int pageSize);

    /**
     * 查询场地可用时间段
     */
    List<VenueBooking> getAvailableSlots(Long venueId, LocalDate date);

    /**
     * 提交预约申请
     */
    void submitBooking(VenueBooking booking);

    /**
     * 审批预约
     */
    void approveBooking(Long bookingId, Long approverId, boolean approved, String remark);

    /**
     * 取消预约
     */
    void cancelBooking(Long bookingId);
}
