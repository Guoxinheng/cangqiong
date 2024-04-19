package com.controller.admin;
import com.constant.dto.CancelOrdersDTO;
import com.constant.dto.ConfirmDTO;
import com.constant.dto.PageOrdersDTO;
import com.constant.dto.RejectionOrderDTO;
import com.result.Result;
import com.service.OrdersService;
import com.vo.OrderDetailListVO;
import com.vo.PageOrdersSearchVO;
import com.vo.StatisticsNumberVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController("adminController")
@RequestMapping("/admin/order")
@Api(tags = "订单管理")
@Slf4j
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 查看订单详情
     *
     * @param id 订单的唯一标识符
     * @return 返回订单详情的结果，其中包括订单的详细信息
     */
    @GetMapping("/details/{id}")
    @ApiOperation("查看订单详情")
    public Result<OrderDetailListVO> details(@PathVariable Long id) {
        log.info("查看订单详情:{}", id); // 记录查看订单详情的日志
        OrderDetailListVO orderDetailListVO = new OrderDetailListVO();
        orderDetailListVO = ordersService.getOrdersDetailListById(id); // 通过订单ID获取订单详情列表
        return Result.success(orderDetailListVO); // 返回获取到的订单详情
    }

    /**
     * 条件查询订单
     *
     * @param pageOrdersDTO 包含查询条件和分页信息的数据传输对象
     * @return 返回查询结果的包装对象，包含订单的详细信息
     */
    @GetMapping("conditionSearch")
    @ApiOperation("条件查询订单")
    public Result<PageOrdersSearchVO> conditionSearch(PageOrdersDTO pageOrdersDTO) {
        log.info("条件查询订单:{}", pageOrdersDTO);
        PageOrdersSearchVO pageOrdersSearchVO = new PageOrdersSearchVO();
        pageOrdersSearchVO = ordersService.conditionSearch(pageOrdersDTO);
        return Result.success(pageOrdersSearchVO);
    }

    /**
     * 拒绝订单操作。
     *
     * @param rejectionOrderDTO 拒单信息传输对象，包含需要拒单的详细信息。
     * @return 返回操作结果，成功则返回"拒单成功"的消息。
     */
    @PutMapping("/rejection")
    @ApiOperation("拒单")
    public Result<String> rejection(@RequestBody RejectionOrderDTO rejectionOrderDTO) {
        // 记录拒单操作的日志
        log.info("拒单:{}", rejectionOrderDTO);
        // 调用服务层方法，执行拒单逻辑
        ordersService.rejection(rejectionOrderDTO);
        // 返回成功结果
        return Result.success("拒单成功");
    }

    /**
     * 订单统计接口
     * 无参数
     *
     * @return Result<StatisticsNumberVO> 返回订单统计结果，包含订单统计信息的封装对象
     */
    @GetMapping("/statistics")
    @ApiOperation("订单统计")
    public Result<StatisticsNumberVO> statistics() {
        // 记录订单统计操作的日志
        log.info("订单统计");
        // 调用服务层方法，获取订单统计信息
        StatisticsNumberVO statisticsNumberVO = ordersService.statistics();
        // 返回订单统计结果
        return Result.success(statisticsNumberVO);
    }

    /**
     * 接单操作。
     * 对于给定的订单ID，确认接单。
     *
     * @param confirmDTO 订单的唯一标识符。
     * @return 返回一个结果对象，包含接单成功的消息。
     */
    @PutMapping("/confirm")
    @ApiOperation("接单")
    public Result<String> confirm(@RequestBody ConfirmDTO confirmDTO) {
        log.info("接单:{}", confirmDTO); // 记录接单操作的日志
        ordersService.confirm(confirmDTO.getId()); // 调用服务层方法，确认接单
        return Result.success("用户接单成功"); // 返回成功结果
    }

    /**
     * 派送订单
     *
     * @param id 订单的唯一标识符
     * @return 返回一个结果对象，包含派送操作的成功信息
     */
    @PutMapping("/delivery/{id}")
    @ApiOperation("派送订单")
    public Result<String> deliver(@PathVariable Long id) {
        log.info("派送订单:{}", id); // 记录派送订单操作的日志
        ordersService.deliver(id); // 调用服务层方法，确认派送订单
        return Result.success("派送订单成功"); // 返回成功结果
    }
    /**
     * 完成指定订单的操作。
     *
     * @param id 订单的唯一标识符，通过路径变量传递。
     * @return 返回一个结果对象，包含完成订单的成功信息。
     */
    @PutMapping("/complete/{id}")
    @ApiOperation("完成订单")
    public Result<String> complete(@PathVariable Long id) {
        log.info("完成订单:{}", id); // 记录日志，说明订单完成操作的执行，包括订单ID。
        ordersService.complete(id); // 调用服务层方法以完成指定订单
        return Result.success("完成订单成功"); // 返回订单成功完成的结果
    }
    /**
     * 取消订单
     *
     * @param cancelOrdersDTO 包含需要取消的订单信息的数据传输对象，其中必须包含订单ID。
     * @return 返回一个表示操作结果的Result对象，包含取消操作的成功信息。
     */
    @PutMapping("/cancel")
    public Result<String> cancel(@RequestBody CancelOrdersDTO cancelOrdersDTO)
    {
        // 通过订单服务，根据传入的订单ID取消订单
        ordersService.cancaelOrderByIdBecauseAdmin(cancelOrdersDTO);
        // 返回成功结果，包含订单取消成功的消息
        return Result.success("订单取消成功");
    }
}
