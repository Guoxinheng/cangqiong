package com.controller.user;

import com.dto.OrderSubmitDTO;
import com.dto.OrdersPaymentDTO;
import com.result.Result;
import com.service.OrdersService;
import com.vo.OrderPaymentVO;
import com.vo.OrderSubmitVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        return Result.success(orderPaymentVO);
    }
}
