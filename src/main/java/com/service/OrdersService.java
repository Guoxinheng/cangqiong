package com.service;

import com.dto.OrderSubmitDTO;
import com.dto.OrdersListDTO;
import com.dto.OrdersPaymentDTO;
import com.vo.ListOrdersVO;
import com.vo.OrderPaymentVO;
import com.vo.OrderSubmitVO;
import com.vo.PageOrdersVO;
public interface OrdersService {
    OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

     void paySuccess(String outTradeNo);

    PageOrdersVO historyOrders(OrdersListDTO ordersListDTO);

    ListOrdersVO getOrdersDetail(Long id);

    void repetition(Long id);

    void cancaelOrderById(Long id);

    void reminder(Long id);
}
