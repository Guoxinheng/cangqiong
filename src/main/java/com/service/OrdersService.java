package com.service;

import com.dto.*;
import com.vo.*;

public interface OrdersService {
    OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

     void paySuccess(String outTradeNo);

    PageOrdersVO historyOrders(OrdersListDTO ordersListDTO);

    ListOrdersVO getOrdersDetail(Long id);

    void repetition(Long id);

    void cancaelOrderById(Long id);

    void reminder(Long id);

    PageOrdersSearchVO conditionSearch(PageOrdersDTO pageOrdersDTO);

    OrderDetailListVO getOrdersDetailListById(Long id);

    void rejection(RejectionOrderDTO rejectionOrderDTO);

    StatisticsNumberVO statistics();

    void confirm(Long id);

    void deliver(Long id);

    void complete(Long id);

    void cancaelOrderByIdBecauseAdmin(CancelOrdersDTO cancelOrdersDTO);
}
