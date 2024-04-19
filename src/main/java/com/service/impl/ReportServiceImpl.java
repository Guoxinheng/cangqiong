package com.service.impl;
import com.constant.dto.OrderReportVO;
import com.entity.Orders;
import com.mapper.OrdersMapper;
import com.mapper.UserMapper;
import com.service.ReportService;
import com.service.WorkspaceService;
import com.vo.BusinessDataVO;
import com.vo.TurnoverReportVO;
import com.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
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
    @Autowired
    private WorkspaceService workspaceService;
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

    @Override
    public void export(HttpServletResponse response) {
        LocalDate now = LocalDate.now();
        LocalDate begin = now.minusDays(30);
        LocalDate end = now.minusDays(1);
        //查询出今日数据
        BusinessDataVO businessDataVO=workspaceService.businessData(begin,end);
        InputStream resource = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");
        try {
            XSSFWorkbook excel=new XSSFWorkbook(resource);
            XSSFSheet sheet = excel.getSheet("sheet1");
            sheet.getRow(1).getCell(1).setCellValue("时间"+begin.toString()+"至"+end.toString()+"营业数据报告");
            sheet.getRow(3).getCell(2).setCellValue(businessDataVO.getTurnover());//营业额
            sheet.getRow(3).getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());//订单完成率
            sheet.getRow(3).getCell(6).setCellValue(businessDataVO.getNewUsers());//新增用户
            sheet.getRow(4).getCell(2).setCellValue(businessDataVO.getValidOrderCount());//有效订单
            sheet.getRow(4).getCell(4).setCellValue(businessDataVO.getUnitPrice());//  平均客单价

            for (int i = 0; i < 30; i++)
            {
                BusinessDataVO businessDataVO1 = workspaceService.businessData(begin.plusDays(i), begin.plusDays(i));
                sheet.getRow(7+i).getCell(1).setCellValue(String.valueOf(begin.plusDays(i)));//日期
                sheet.getRow(7+i).getCell(2).setCellValue(businessDataVO1.getTurnover());//营业额
                sheet.getRow(7+i).getCell(3).setCellValue(businessDataVO1.getValidOrderCount());//有效订单
                sheet.getRow(7+i).getCell(4).setCellValue(businessDataVO1.getOrderCompletionRate());//订单完成率
                sheet.getRow(7+i).getCell(5).setCellValue(businessDataVO1.getUnitPrice());//平均客单价
                sheet.getRow(7+i).getCell(6).setCellValue(businessDataVO1.getNewUsers());//新增用户数
            }

            ServletOutputStream outputStream = response.getOutputStream();
            excel.write(outputStream);
            resource.close();
            excel.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
