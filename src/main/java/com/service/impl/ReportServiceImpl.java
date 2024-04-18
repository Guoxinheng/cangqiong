package com.service.impl;
import com.dto.OrderReportVO;
import com.entity.Orders;
import com.mapper.OrdersMapper;
import com.mapper.UserMapper;
import com.service.ReportService;
import com.vo.TurnoverReportVO;
import com.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
     private   OrdersMapper ordersMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * 统计指定日期范围内的营业额报告。
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 转营业额报告的视图对象，包含日期列表和对应日期的营业额列表。
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        TurnoverReportVO turnoverReportVO=new TurnoverReportVO();
        List<LocalDate> dateList=new ArrayList<>();
        // 生成日期列表，从开始日期到结束日期（包含结束日期）
        while(!begin.equals(end))
        {
            dateList.add(begin);
            begin=begin.plusDays(1);
        }
        dateList.add(begin);

        // 计算每个日期对应的营业额
        List<Double> amountlist=new ArrayList<>();
        for (LocalDate date : dateList) {
            LocalDateTime beginTime=LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(date,LocalTime.MAX);
            Integer status= Orders.COMPLETED_ORDER;
            // 查询指定日期内的订单总金额
            Double  amount= ordersMapper.getSumAmountByDate(beginTime,endTime,status);
            amount=amount==null ? 0.0 : amount;
            amountlist.add(amount);
        }

        // 将日期列表和金额列表转化为字符串
        String dateString = StringUtils.join(dateList, ",");
        String amountString = StringUtils.join(amountlist, ",");
        // 设置营业额报告的日期列表和金额列表
        turnoverReportVO.setTurnoverList(amountString);
        turnoverReportVO.setDateList(dateString);

        return turnoverReportVO;
    }


    /**
     * 统计指定日期范围内的用户报告。
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return 转用户报告的视图对象，包含日期列表和对应日期的新增用户数量列表。
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        UserReportVO userReportVO=new UserReportVO();
        List<LocalDate> dateList=new ArrayList<>();
        while(!begin.equals(end))
        {
            dateList.add(begin);
            begin=begin.plusDays(1);
        }
        dateList.add(begin);
        String dateString = StringUtils.join(dateList, ",");
        userReportVO.setDateList(dateString);
        List<Integer> totalUserList=new ArrayList<>();
        List<Integer> newUserList=new ArrayList<>();
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime=LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(localDate,LocalTime.MAX);
            //计算总用户数
            Integer totalUser= userMapper.getUserAmount(null,null);
            totalUser=totalUser==null ? 0 : totalUser;
            totalUserList.add(totalUser);
            //今日的用户数量减去昨日的用户数量
            Integer newUser= userMapper.getUserAmount(beginTime,endTime);
            newUser=newUser==null ? 0 : newUser;
            newUserList.add(newUser);
        }
        String allUserString = StringUtils.join(totalUserList, ",");
        String newUserString = StringUtils.join(newUserList, ",");
        userReportVO.setTotalUserList(allUserString);
        userReportVO.setDateList(dateString);
        userReportVO.setNewUserList(newUserString);
        return userReportVO;
    }

    /**
     * 统计指定日期范围内的订单数据。
     *
     * @param begin 开始日期
     * @param end 结束日期
     * @return OrderReportVO 订单统计报告，包含日期列表、每日订单数、每日有效订单数及订单完成率等信息。
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {

        OrderReportVO orderReportVO=new OrderReportVO();
        List<LocalDate> dateList=new ArrayList<>();
            // 生成日期范围内的所有日期列表
            while(!begin.equals(end))
            {
                dateList.add(begin);
                begin=begin.plusDays(1);
            }
            dateList.add(begin);
            // 将日期列表转化为字符串
            String dateString = StringUtils.join(dateList, ",");
        orderReportVO.setDateList(dateString);

        // 初始化订单数量和有效订单数量的列表
        List<Double> orderCountList=new ArrayList<>();
        List<Double> validOrderCountList=new ArrayList<>();
        Double totalOrderCount=0.0; // 总订单数量
        Double totalValidOrderCount=0.0; // 总有效订单数量

        // 遍历日期列表，统计每天的订单数和有效订单数
        for (LocalDate localDate : dateList) {
            LocalDateTime beginTime=LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime=LocalDateTime.of(localDate,LocalTime.MAX);
            // 获取每天的订单数量
            Double orderCount=ordersMapper.getOrderCount(beginTime,endTime);
            // 获取每天的有效订单数量
            Double validOrderCount=ordersMapper.getValidOrderCount(beginTime,endTime,Orders.COMPLETED_ORDER);
            totalOrderCount=totalOrderCount+orderCount;
            totalValidOrderCount=totalValidOrderCount+validOrderCount;
            // 将订单数量和有效订单数量添加到对应的列表中
            orderCountList.add(orderCount);
            validOrderCountList.add(validOrderCount);
        }
        // 将订单数量和有效订单数量的列表转化为字符串
        orderReportVO.setOrderCountList(StringUtils.join(orderCountList, ","));
        orderReportVO.setValidOrderCountList(StringUtils.join(validOrderCountList, ","));
        // 设置订单完成率
        orderReportVO.setOrderCompletionRate(totalValidOrderCount/totalOrderCount);
        orderReportVO.setValidOrderCount(totalValidOrderCount);
        orderReportVO.setTotalOrderCount(totalOrderCount);
        return orderReportVO;
    }

}
