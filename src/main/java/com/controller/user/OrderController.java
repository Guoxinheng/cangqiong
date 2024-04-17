package com.controller.user;

import com.dto.OrderSubmitDTO;
import com.dto.OrdersListDTO;
import com.dto.OrdersPaymentDTO;
import com.github.pagehelper.Page;
import com.result.Result;
import com.service.OrdersService;
import com.vo.ListOrdersVO;
import com.vo.OrderPaymentVO;
import com.vo.OrderSubmitVO;
import com.vo.PageOrdersVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import static com.constant.MessageConstant.orderid;

@RestController
@RequestMapping("/user/order")
@Api(tags = "用户订单管理")
@Slf4j
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/submit")
    @ApiOperation("用户下单")
    public Result<OrderSubmitVO> submit(@RequestBody OrderSubmitDTO orderSubmitDTO)
    {
        OrderSubmitVO orderSubmitVO= ordersService.submit(orderSubmitDTO);

        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    @ApiOperation("订单支付")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        log.info("订单支付：{}", ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = ordersService.payment(ordersPaymentDTO);
        log.info("生成预支付交易单：{}", orderPaymentVO);
        ordersService.paySuccess(String.valueOf(orderid));
        return Result.success(orderPaymentVO);
    }
    //历史订单查询
    @GetMapping("/historyOrders")
    @ApiOperation("历史订单查询")
    public Result<PageOrdersVO>  historyOrders(OrdersListDTO ordersListDTO) {

        log.info("历史订单查询{}",ordersListDTO);
        //根据订单id查询订单详情
        PageOrdersVO list=ordersService.historyOrders(ordersListDTO);

        return Result.success(list);
    }
    /**
     * 根据订单ID获取订单详情
     *
     * @param id 订单ID，通过路径变量传递
     * @return 返回订单详情的结果对象，其中包含订单详情信息
     */
    @GetMapping("/orderDetail/{id}")
    public Result<ListOrdersVO>  getOrdersDetail(@PathVariable Long id)
    {
      //根据订单id查询订单详情
        ListOrdersVO listOrdersVO=ordersService.getOrdersDetail(id);
        return Result.success(listOrdersVO);
    }
    /**
     * 处理再来一单的请求。
     *
     * @param id 订单的唯一标识符，通过路径变量传递。
     * @return 返回一个结果对象，包含再来一单操作的状态信息。
     */
    @PostMapping("/repetition/{id}")
    @ApiOperation("再来一单")
    public Result<String> repetition(@PathVariable Long id)
    {
        log.info("再来一单{}",id);
        ordersService.repetition(id);
        return Result.success("再来一单成功");
    }

    /**
     * 取消订单
     *
     * @param id 订单的唯一标识符，通过路径变量传递。
     * @return 返回一个结果对象，包含取消订单的操作结果信息。
     */
    @PutMapping("/cancel/{id}")
    @ApiOperation("取消订单")
    public Result<String> cancael(@PathVariable Long id)
    {
        // 记录取消订单的日志
        log.info("取消订单{}",id);
        // 调用服务层方法，根据订单ID取消订单
        ordersService.cancaelOrderById(id);
        // 返回成功结果，包含取消订单的信息
        return Result.success("取消订单");
    }
    /**
     * 催单操作
     *
     * @param id 订单的唯一标识符，通过路径变量传递。
     * @return 返回一个结果对象，包含催单操作的成功信息。
     */
    @GetMapping("/reminder/{id}")
    @ApiOperation("催单")
    public Result<String> reminder(@PathVariable Long id)
    {
        // 记录催单操作的日志
        log.info("催单{}",id);
        // 调用服务层方法，执行催单逻辑
        ordersService.reminder(id);
        // 返回成功结果，包含催单成功的消息
        return Result.success("催单成功");
    }


}
