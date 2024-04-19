package com.mapper;

import com.constant.dto.OrderDetailTopDTO;
import com.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderDetailMapper {
    void save(OrderDetail orderDetail);

    /**
     * 根据订单ID获取订单详情列表。
     *
     * @param id 订单ID，类型为Long。这是一个必传参数，用于指定需要查询的订单。
     * @return 返回一个OrderDetail对象，包含了指定订单的所有详情。
     */
    @Select("select * from order_detail where order_id=#{id}")
    List<OrderDetail> getListByOrderId(Long id);

    /**
     * 获取指定时间范围内的订单详情顶部数据传输对象列表。
     *
     * @param beginTime 开始时间，用于筛选订单的起始时间点。
     * @param endTime 结束时间，用于筛选订单的结束时间点。
     * @return 返回一个订单详情顶部数据传输对象列表，包含在指定时间范围内的订单名称和数量等顶部统计信息。
     */
    List<OrderDetailTopDTO> getNameAndNumber(@Param("beginTime") LocalDateTime beginTime,@Param("endTime") LocalDateTime endTime);
}
