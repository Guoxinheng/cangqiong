package com.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderOverViewVO implements Serializable {
     private   Double allOrders;  //全部订单数
    private  Double cancelledOrders;//已取消订单数
    private  Double completedOrders;//已完成订单数
    private  Double deliveredOrders;//待派送订单数
    private Double waitingOrders;//待接单订单数
}
