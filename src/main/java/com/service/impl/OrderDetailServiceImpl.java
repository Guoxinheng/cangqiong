package com.service.impl;

import com.constant.dto.OrderDetailTopDTO;
import com.mapper.OrderDetailMapper;
import com.service.OrderDetailService;
import com.vo.SalesTop10ReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private    OrderDetailMapper orderDetailMapper;
    /**
     * 根据指定日期范围查询订单数量排名前10的销售报告
     * @param begin 开始日期
     * @param end 结束日期
     * @return SalesTop10ReportVO 销售前10报告视图对象，包含姓名和数量列表
     */
    @Override
    public SalesTop10ReportVO ordersTop(LocalDate begin, LocalDate end) {

        // 将开始和结束日期转换为LocalDateTime格式，分别设置为每天的最早和最晚时间
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalDateTime.MIN.toLocalTime());
        LocalDateTime endTime = LocalDateTime.of(end, LocalDateTime.MAX.toLocalTime());

        // 查询指定时间范围内的订单详情，包括姓名和数量
        List<OrderDetailTopDTO> list = orderDetailMapper.getNameAndNumber(beginTime,endTime);

        // 分别提取姓名和数量列表
        List<String> nameList = list.stream().map(OrderDetailTopDTO::getName).collect(Collectors.toList());
        List<Long> numberList = list.stream().map(OrderDetailTopDTO::getNumber).collect(Collectors.toList());

        // 创建销售前10报告视图对象，并设置姓名和数量列表（以逗号分隔的字符串形式）
        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();
        salesTop10ReportVO.setNameList( StringUtils.join(nameList,","));
        salesTop10ReportVO.setNumberList(StringUtils.join(numberList,","));

        return salesTop10ReportVO;
    }

}
