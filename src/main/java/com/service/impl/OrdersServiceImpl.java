package com.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.context.BaseContext;
import com.dto.OrderSubmitDTO;
import com.dto.OrdersPaymentDTO;
import com.entity.*;
import com.exception.OrderBusinessException;
import com.mapper.*;
import com.service.OrdersService;
import com.untils.WeChatPayUtil;
import com.vo.OrderPaymentVO;
import com.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.constant.MessageConstant.orderid;

@Service
public class OrdersServiceImpl implements OrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private AddressBookMapper addressBookMapper;
    @Autowired
    private  UserMapper userMapper;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Override
    //提交订单
    @Transactional
    public OrderSubmitVO submit(OrderSubmitDTO orderSubmitDTO) {
        //向订单表插入1条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(orderSubmitDTO,orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setUserId(BaseContext.getCurrentId());
        orders.setPayStatus(0);
        orders.setStatus(1);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        AddressBook addressBook = addressBookMapper.getAddressById(orderSubmitDTO.getAddressBookId());
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        ordersMapper.save(orders);

        //向订单明细表插入多条数据
        //从用户购物车查出多条数据
        List<ShoppingCart> shoppingCartList= shoppingCartMapper.getShoppingCartListByUserId(BaseContext.getCurrentId());
        //把此次订单明细中此次订单中
        for (ShoppingCart shoppingCart : shoppingCartList) {
            OrderDetail orderDetail=new OrderDetail();
            BeanUtils.copyProperties(shoppingCart,orderDetail);
            orderDetail.setOrderId(orders.getId());
            orderDetailMapper.save(orderDetail);
        }
        //清空当前用户的购物车
        shoppingCartMapper.cleanShoppingCart(BaseContext.getCurrentId());
        //封住返回数据
        OrderSubmitVO orderSubmitVO=new OrderSubmitVO();
        orderSubmitVO.setOrderAmount(orderSubmitDTO.getAmount());
        orderSubmitVO.setOrderNumber(orders.getNumber());
        orderSubmitVO.setOrderTime(LocalDateTime.now());
        orderSubmitVO.setId(orders.getId());
        orderid= Long.valueOf(orders.getNumber());
        return orderSubmitVO;
    }

    /**

     * 订单支付

     *

     * @param ordersPaymentDTO

     * @return

     */

    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {

        // 当前登录用户id

        Long userId = BaseContext.getCurrentId();

        User user = userMapper.getById(userId);


        //调用微信支付接口，生成预支付交易单

//    JSONObject jsonObject = weChatPayUtil.pay(

//        ordersPaymentDTO.getOrderNumber(), //商户订单号

//        new BigDecimal(0.01), //支付金额，单位 元

//        "苍穹外卖订单", //商品描述

//        user.getOpenid() //微信用户的openid

//    );

//

//    if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {

//  throw new OrderBusinessException("该订单已支付");

//    }


        JSONObject jsonObject = new JSONObject();

        jsonObject.put("code", "ORDERPAID");

//


        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);

        vo.setPackageStr(jsonObject.getString("package"));



        //为替代微信支付成功后的数据库订单状态更新，多定义一个方法进行修改

        Integer OrderPaidStatus = 1; //支付状态，已支付

        Integer OrderStatus = 2;  //订单状态，待接单


        //发现没有将支付时间 check_out属性赋值，所以在这里更新

        LocalDateTime check_out_time = LocalDateTime.now();


        ordersMapper.updateStatus(OrderStatus, OrderPaidStatus, check_out_time, orderid);

        return vo;

    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();

        // 根据订单号查询当前用户的订单
        Orders ordersDB = ordersMapper.getByNumberAndUserId(outTradeNo, userId);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = new Orders();
        orders.setId(ordersDB.getId());
        orders.setStatus(2);
        orders.setPayStatus(1);
        orders.setCheckoutTime(LocalDateTime.now());

        ordersMapper.update(orders);

//        //通过websocket向客户端浏览器推送消息 type orderId content
//        Map map = new HashMap();
//        map.put("type",1); // 1表示来单提醒 2表示客户催单
//        map.put("orderId",ordersDB.getId());
//        map.put("content","订单号：" + outTradeNo);
//
//        String json = JSON.toJSONString(map);
//        webSocketServer.sendToAllClient(json);
    }
}
