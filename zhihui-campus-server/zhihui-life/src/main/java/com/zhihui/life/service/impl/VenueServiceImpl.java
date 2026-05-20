package com.zhihui.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zhihui.common.core.domain.PageResult;
import com.zhihui.life.entity.Venue;
import com.zhihui.life.entity.VenueBooking;
import com.zhihui.life.mapper.VenueBookingMapper;
import com.zhihui.life.mapper.VenueMapper;
import com.zhihui.life.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 场地服务实现类
 */
@Service
@RequiredArgsConstructor
public class VenueServiceImpl extends ServiceImpl<VenueMapper, Venue> implements VenueService {

    private final VenueMapper venueMapper;
    private final VenueBookingMapper venueBookingMapper;

    @Override
    public List<Venue> getVenueList(String venueType) {
        LambdaQueryWrapper<Venue> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Venue::getStatus, 1);
        if (StringUtils.hasText(venueType)) {
            wrapper.eq(Venue::getVenueType, venueType);
        }
        wrapper.orderByAsc(Venue::getSortOrder);
        return venueMapper.selectList(wrapper);
    }

    @Override
    public PageResult<VenueBooking> getBookingList(Long userId, Integer status, int pageNum, int pageSize) {
        Page<VenueBooking> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<VenueBooking> wrapper = new LambdaQueryWrapper<>();

        if (userId != null) {
            wrapper.eq(VenueBooking::getUserId, userId);
        }
        if (status != null) {
            wrapper.eq(VenueBooking::getStatus, status);
        }
        wrapper.orderByDesc(VenueBooking::getCreateTime);

        Page<VenueBooking> result = venueBookingMapper.selectPage(page, wrapper);
        return new PageResult<>(result.getRecords(), result.getTotal(), pageNum, pageSize);
    }

    @Override
    public List<VenueBooking> getAvailableSlots(Long venueId, LocalDate date) {
        LambdaQueryWrapper<VenueBooking> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VenueBooking::getVenueId, venueId)
                .eq(VenueBooking::getBookingDate, date)
                .in(VenueBooking::getStatus, 0, 1); // 待审批或已通过
        return venueBookingMapper.selectList(wrapper);
    }

    @Override
    @Transactional
    public void submitBooking(VenueBooking booking) {
        // 检查场地是否存在
        Venue venue = venueMapper.selectById(booking.getVenueId());
        if (venue == null) {
            throw new RuntimeException("场地不存在");
        }

        // 检查是否与已有预约冲突
        List<VenueBooking> existingBookings = getAvailableSlots(
                booking.getVenueId(), booking.getBookingDate());
        for (VenueBooking existing : existingBookings) {
            if (booking.getStartTime().isBefore(existing.getEndTime()) &&
                    booking.getEndTime().isAfter(existing.getStartTime())) {
                throw new RuntimeException("该时间段已被预约");
            }
        }

        booking.setStatus(0); // 待审批
        booking.setCreateTime(LocalDateTime.now());

        venueBookingMapper.insert(booking);
    }

    @Override
    @Transactional
    public void approveBooking(Long bookingId, Long approverId, boolean approved, String remark) {
        VenueBooking booking = venueBookingMapper.selectById(bookingId);
        if (booking == null) {
            throw new RuntimeException("预约不存在");
        }
        if (booking.getStatus() != 0) {
            throw new RuntimeException("预约状态不正确");
        }

        booking.setStatus(approved ? 1 : 2); // 1已通过 2已拒绝
        booking.setApproverId(approverId);
        booking.setApproveRemark(remark);
        booking.setApproveTime(LocalDateTime.now());
        booking.setUpdateTime(LocalDateTime.now());

        venueBookingMapper.updateById(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        VenueBooking booking = venueBookingMapper.selectById(bookingId);
        if (booking == null) {
            throw new RuntimeException("预约不存在");
        }
        if (booking.getStatus() >= 2) {
            throw new RuntimeException("预约已审批，无法取消");
        }

        booking.setStatus(3); // 已取消
        booking.setUpdateTime(LocalDateTime.now());

        venueBookingMapper.updateById(booking);
    }
}
