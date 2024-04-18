package com.controller.admin;


import com.dto.OrderReportVO;
import com.result.Result;
import com.service.OrderDetailService;
import com.service.ReportService;
import com.vo.SalesTop10ReportVO;
import com.vo.TurnoverReportVO;
import com.vo.UserReportVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;


@RestController
@RequestMapping("/admin/report")
@Api(tags = "订单报表")
@Slf4j
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private OrderDetailService orderDetailService;
    /**
     * 营业额统计接口
     *
     * @param begin 开始日期，格式为"yyyy-MM-dd"
     * @param end 结束日期，格式为"yyyy-MM-dd"
     * @return 返回一个包含营业额报告的结果对象，其中包含统计时间段内的营业额报告信息
     */
    @GetMapping("/turnoverStatistics")
    @ApiOperation("营业额统计接口")
    public Result<TurnoverReportVO> turnoverStatistics (@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end)
    {
        log.info("开始时间：{}，结束时间：{}",begin,end); // 记录查询开始和结束时间
        TurnoverReportVO turnoverReportVO=new TurnoverReportVO();
        turnoverReportVO=  reportService.turnoverStatistics(begin,end); // 调用服务层方法，获取营业额统计信息
        return  Result.success(turnoverReportVO) ; // 返回成功响应，包含营业额报告
    }
    /**
     * 用户统计接口
     *
     * @param begin 开始日期，格式为yyyy-MM-dd
     * @param end 结束日期，格式为yyyy-MM-dd
     * @return 返回用户统计结果的Result对象，其中包含UserReportVO类型的统计报告
     */
    @GetMapping("/userStatistics")
    @ApiOperation("用户统计接口")
    public Result<UserReportVO> userStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end) {
        log.info("开始时间：{}，结束时间：{}",begin,end);
        // 创建用户报告的VO实例
        UserReportVO userReportVO = new UserReportVO();
        // 调用service层获取用户统计信息，更新userReportVO实例
        userReportVO = reportService.userStatistics(begin, end);
        // 返回成功响应，携带用户统计报告
        return Result.success(userReportVO);
    }
    /**
     * 获取指定时间范围内的订单统计信息。
     *
     * @param begin 开始日期，格式为"yyyy-MM-dd"
     * @param end 结束日期，格式为"yyyy-MM-dd"
     * @return 返回订单统计结果，包含在Result<OrderReportVO>中，其中OrderReportVO是订单报告的视图对象
     */
    @GetMapping("/ordersStatistics")
    @ApiOperation("获取指定时间范围内的订单统计信息")
    public Result<OrderReportVO> ordersStatistics(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end)
    {
        log.info("开始时间：{}，结束时间：{}",begin,end);
        // 初始化订单报告视图对象
        OrderReportVO orderReportVO=new OrderReportVO();
        // 调用服务层方法，获取订单统计信息并填充到视图对象中
        orderReportVO=reportService.ordersStatistics(begin,end);
        // 返回订单统计结果
        return Result.success(orderReportVO);
    }
   /**
    * 查询指定时间段内销量排名前10的订单信息。
    *
    * @param begin 开始日期，格式为"yyyy-MM-dd"
    * @param end 结束日期，格式为"yyyy-MM-dd"
    * @return 返回销量排名前10的订单信息，封装在Result<SalesTop10ReportVO>中，其中SalesTop10ReportVO包含具体的订单详情。
    */
   @GetMapping("/top10")
   @ApiOperation("查询销量排名top10")
   public Result<SalesTop10ReportVO> ordersTop(@DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin, @DateTimeFormat(pattern = "yyyy-MM-dd")LocalDate end)
   {
       log.info("开始时间：{}，结束时间：{}",begin,end); // 记录查询的开始和结束时间
       SalesTop10ReportVO salesTop10ReportVO=new SalesTop10ReportVO();
       // 调用服务层方法，获取销量排名前10的订单信息
       salesTop10ReportVO= orderDetailService.ordersTop(begin,end);
      return  Result.success(salesTop10ReportVO); // 返回查询结果
   }

}
