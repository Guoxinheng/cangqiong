package com.service;

import com.dto.OrderSubmitDTO;
import com.dto.OrdersPaymentDTO;
import com.vo.OrderPaymentVO;
import com.vo.OrderSubmitVO;

public interface OrdersService {
    OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO);

    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

     void paySuccess(String outTradeNo);
}
