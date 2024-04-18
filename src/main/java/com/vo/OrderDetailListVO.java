package com.vo;

import com.entity.OrderDetail;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailListVO implements Serializable {
    String address;//地址
    Long addressBookId;//地址id
    BigDecimal amount;//实收金额
    String cancelReason;//订单取消原因
    LocalDateTime cancelTime;//订单取消时间
    LocalDateTime checkoutTime;//结账时间
    String consignee;//收货人
    Integer deliveryStatus;//配送状态 1立即送出 0选择时间送达
    LocalDateTime deliveryTime;//送达时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime estimatedDeliveryTime;//预计送达时间
    Long id;//主键
    String number;//订单号
    LocalDateTime orderTime;//下单时间
    BigDecimal packAmount;//打包费
    Integer payMethod;//支付方式,支付宝还是微信
    Integer payStatus;//支付状态 0未支付 1已支付 2退款
    String phone;//手机号
    String rejectionReason;//订单拒绝原因
    String remark;//备注
    Integer status;//订单状态
    Long userId;//下单用户
    String userName;//用户名称
    Integer tablewareNumber;//餐具数量
    Integer tablewareStatus;//餐具数量状态 1按量提供 0整单打包
    List<OrderDetail> orderDetailList;
    String orderDishes;//将菜品信息拼成一个长字符串
}
