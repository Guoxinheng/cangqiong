package com.mapper;

import com.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDetailMapper {
    void save(OrderDetail orderDetail);
}
