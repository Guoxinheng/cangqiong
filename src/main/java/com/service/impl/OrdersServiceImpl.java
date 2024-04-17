package com.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.context.BaseContext;
import com.dto.OrderSubmitDTO;
import com.dto.OrdersListDTO;
import com.dto.OrdersPaymentDTO;
import com.entity.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mapper.*;
import com.service.OrdersService;
import com.untils.WeChatPayUtil;
import com.vo.ListOrdersVO;
import com.vo.OrderPaymentVO;
import com.vo.OrderSubmitVO;
import com.vo.PageOrdersVO;
import com.websocket.WebSocketServer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;

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
    @Autowired
    private WebSocketServer webSocketServer;
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

        //通过websocket向客户端浏览器推送消息 type orderId content
        Map map = new HashMap();
        map.put("type",1); // 1表示来单提醒 2表示客户催单
        map.put("orderId",ordersDB.getId());
        map.put("content","订单号：" + outTradeNo);

        String json = JSON.toJSONString(map);
        webSocketServer.sendToAllClient(json);

    }
    /**
     * 查询用户的历史订单
     *

     * @return 返回根据条件查询到的订单列表，如果方法实现暂不提供，返回null
     */
    @Override
    public PageOrdersVO historyOrders(OrdersListDTO ordersListDTO) {
        List<ListOrdersVO> listOrdersVOS=new ArrayList<>();
        PageHelper.startPage(ordersListDTO.getPage(), ordersListDTO.getPageSize());
        Long userId=BaseContext.getCurrentId();
        Page<Orders> list=ordersMapper.getHistoryOrders(ordersListDTO.getStatus(),userId);
        List<Orders> result = list.getResult();
        for (Orders orders : result) {
            ListOrdersVO listOrdersVO=new ListOrdersVO();
         List<OrderDetail> orderDetail= orderDetailMapper.getListByOrderId(orders.getId());
         if (orderDetail!=null&&orderDetail.size()>0)
         {
             listOrdersVO.setOrderDetailList(orderDetail);
         }

            BeanUtils.copyProperties(orders,listOrdersVO);

            listOrdersVOS.add(listOrdersVO);
        }
        PageOrdersVO pageOrdersVO=new PageOrdersVO();
        pageOrdersVO.setRecords(listOrdersVOS);
        pageOrdersVO.setTotal(list.getTotal());
        return pageOrdersVO;
    }
    /**
     * 获取订单详情
     *
     * @param id 订单的唯一标识符
     * @return 返回订单详情的集合，包含订单的各种信息。这里返回null是示例，实际应用中应返回具体订单详情。
     */
    @Override
     public ListOrdersVO getOrdersDetail(Long id) {
        ListOrdersVO listOrdersVO=new ListOrdersVO();
        Orders orders=ordersMapper.getById(id);
        List<OrderDetail> orderDetail=orderDetailMapper.getListByOrderId(id);
        if (orderDetail!=null&&orderDetail.size()>0)
        {
            listOrdersVO.setOrderDetailList(orderDetail);
        }
        BeanUtils.copyProperties(orders,listOrdersVO);
        return listOrdersVO;
    }
    /**
     * 重复执行某个操作。
     * 该方法将根据提供的ID重复执行某项操作，但当前方法体为空，需要具体实现操作内容。
     *
     * @param id 一个长整型的ID，用于标识需要重复执行的操作的对象或实体。
     * @return 无返回值。
     */
    @Override
    public void repetition(Long id) {
        //首先根据id查询该订单
        //该订单中的购物信息转换到购物车
        Orders orders=ordersMapper.getById(id);
        List<OrderDetail> orderDetailList=orderDetailMapper.getListByOrderId(id);
        for (OrderDetail orderDetail : orderDetailList) {
            ShoppingCart shoppingCart=new ShoppingCart();
            BeanUtils.copyProperties(orderDetail,shoppingCart);
            shoppingCart.setUserId(orders.getUserId());
            shoppingCart.setDishFlavor(orderDetail.getDishFlavor());
            shoppingCart.setNumber(orderDetail.getNumber());
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartMapper.saveShoppingCart(shoppingCart);
        }

    }

    /**
     * 根据订单ID取消订单。
     * 该方法通过调用ordersMapper的cancelOrderById方法，将指定订单状态更新为已取消。
     *
     * @param id 订单的唯一标识符，类型为Long。用于指定需要取消的订单。
     * @return 无返回值。该操作仅修改数据库中的订单状态，不返回任何结果。
     */
    @Override
    public void cancaelOrderById(Long id) {
        // 设置订单状态为已取消
        Integer Status=Orders.ORDER_CANCELLED;
        Orders orders=new Orders();
        orders.setId(id);
        orders.setStatus(Status);
        orders.setCancelTime(LocalDateTime.now());
        orders.setCancelReason("用户取消");
        // 调用mapper层，根据订单ID取消订单
        ordersMapper.cancelOrderById(orders);

    }

    /**
     * 提醒功能的实现，针对特定订单向所有客户端发送提醒信息。
     * @param id 订单的唯一标识符。
     */
    @Override
    public void reminder(Long id) {
        // 根据订单ID查询订单信息
        Orders orders = ordersMapper.getById(id);

        // 创建并封装提醒信息的map对象
        Map<String, Object> map = new HashMap<>();
        map.put("type", 2); // 设置提醒类型为2
        map.put("orderId", orders.getId()); // 设置订单ID
        map.put("content", "订单号:" + orders.getNumber()); // 设置提醒内容为订单号

        // 将map对象转换为JSON字符串，以便于通过WebSocket发送给所有客户端
        String json = JSON.toJSONString(map);

        // 向所有客户端发送提醒信息
        webSocketServer.sendToAllClient(json);
    }

}
