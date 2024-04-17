package com.task;

import com.entity.Orders;
import com.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrdersMapper ordersMapper;
   /*
   * 处理超时订单的方法
   *
   * */
    @Scheduled(cron =" 0 * * * * ?  " )//每分钟触发一次
    public void processTimeOutOrder()
    {
        log.info("处理超时订单{}", LocalDateTime.now());
       List<Orders> ordersList= ordersMapper.processTimeOutOrderAndCancelOrder(Orders.PENDING_PAYMENT,LocalDateTime.now().minusMinutes(15));
       if (ordersList!=null&&ordersList.size()>0)
       {
           for (Orders orders : ordersList) {
               orders.setStatus(Orders.ORDER_CANCELLED);//把订单状态设置为已取消
               orders.setRejectionReason("订单超时，自动取消");
               orders.setCancelTime(LocalDateTime.now());
               ordersMapper.update(orders);
           }
       }
    }
    @Scheduled(cron = "0 0 1 * * ? ")//凌晨一点触发一次
    public void processCancelOrder()
    {
        log.info("处理自动取消订单{}",LocalDateTime.now());
        //先查出状态为派送的订单,然后把订单的状态变成完成
        List<Orders> ordersList=ordersMapper.processTimeOutOrderAndCancelOrder(Orders.DELIVERY_IN_PROGRESS,LocalDateTime.now().minusMinutes(60));
        for (Orders orders : ordersList) {
            orders.setStatus(Orders.COMPLETED_ORDER);//状态变成订单完成
            ordersMapper.update(orders);
        }
    }
}
