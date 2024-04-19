package com.service.impl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.constant.dto.*;
import com.context.BaseContext;
import com.dto.*;
import com.entity.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mapper.*;
import com.service.OrdersService;
import com.untils.WeChatPayUtil;
import com.vo.*;
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
        orders.setAddress(addressBook.getProvinceName()+addressBook.getCityName()+addressBook.getDistrictName()+addressBook.getDetail());
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

    /**
     * 根据条件进行订单搜索，并分页返回搜索结果。
     *
     * @param pageOrdersDTO 包含搜索条件和分页信息的数据传输对象，如订单号、手机号、下单时间范围、页码和每页数量等。
     * @return PageOrdersSearchVO 包含总记录数和订单详情列表的视图对象。
     */
    @Override
    public PageOrdersSearchVO conditionSearch(PageOrdersDTO pageOrdersDTO) {

        // 开始分页查询
        PageHelper.startPage(pageOrdersDTO.getPage(), pageOrdersDTO.getPageSize());
        Orders orders = new Orders();
        // 将搜索条件复制到订单实体对象
        BeanUtils.copyProperties(pageOrdersDTO, orders);
        orders.setOrderTime(pageOrdersDTO.getBeginTime());
        orders.setDeliveryTime(pageOrdersDTO.getEndTime());
        // 根据条件搜索订单
        Page<Orders> list = ordersMapper.conditionSearch(orders);

        List<OrderDetailListVO> orderDetailListVOS = new ArrayList<>();
        // 遍历搜索结果，转换为视图对象
        for (Orders orders1 : list.getResult()) {
            OrderDetailListVO orderDetailListVO = new OrderDetailListVO();
            BeanUtils.copyProperties(orders1, orderDetailListVO);
            // 查询订单内的菜品详情
            List<OrderDetail> listByOrderId = orderDetailMapper.getListByOrderId(orders1.getId());
            String s = "";
            // 组合菜品名称和数量
            for (OrderDetail orderDetail : listByOrderId) {
                s = s + orderDetail.getName() + "*" + orderDetail.getNumber() + ";";
            }
            // 设置订单菜品详情
            orderDetailListVO.setOrderDishes(s);
            orderDetailListVOS.add(orderDetailListVO);
        }

        // 构建并返回搜索结果的视图对象
        PageOrdersSearchVO pageOrdersSearchVO = new PageOrdersSearchVO();
        pageOrdersSearchVO.setTotal(list.getTotal());
        pageOrdersSearchVO.setRecords(orderDetailListVOS);
        return pageOrdersSearchVO;
    }

    /**
     * 根据订单ID获取订单详情列表
     * @param id 订单ID
     * @return 返回订单详情列表的视图对象
     */
    @Override
    public OrderDetailListVO getOrdersDetailListById(Long id) {
        OrderDetailListVO orderDetailListVO=new OrderDetailListVO();
        // 通过订单ID查询订单基本信息
        Orders orders=ordersMapper.getById(id);
        // 根据订单ID查询订单包含的菜品详情
        List<OrderDetail> listByOrderId = orderDetailMapper.getListByOrderId(id);
        String s = "";
        // 循环遍历订单中的菜品详情，进行字符串拼接
        for (OrderDetail orderDetail : listByOrderId) {
            s = s + orderDetail.getName() + "*" + orderDetail.getNumber() + ";";
        }
        // 将订单对象的属性值复制到订单详情列表视图对象中
        BeanUtils.copyProperties(orders,orderDetailListVO);
        // 设置订单所包含的菜品详情字符串
        orderDetailListVO.setOrderDishes(s);
        // 设置订单详情列表
        orderDetailListVO.setOrderDetailList(listByOrderId);

        return orderDetailListVO;
    }

    /**
     * 处理拒绝订单的操作。
     * 更新订单状态为已取消，记录拒绝原因和取消时间，并更新到数据库中。
     *
     * @param rejectionOrderDTO 拒绝订单的数据传输对象，包含订单ID、拒绝原因等信息。
     */
    @Override
    public void rejection(RejectionOrderDTO rejectionOrderDTO) {
        // 创建订单对象并设置相关属性
        Orders orders=new Orders();
        orders.setId(rejectionOrderDTO.getId());
        orders.setStatus(Orders.ORDER_CANCELLED); // 设置订单状态为已取消
        orders.setRejectionReason(rejectionOrderDTO.getRejectionReason()); // 设置拒绝原因
        orders.setCancelTime(LocalDateTime.now()); // 设置取消时间为当前时间
        // 调用mapper层，将更新后的订单信息保存到数据库
        ordersMapper.rejection(orders);
    }

    @Override
    public StatisticsNumberVO statistics() {
        //根据订单状态查询各种数据
        List<Orders> list=ordersMapper.getOrdersList();
        StatisticsNumberVO statisticsNumberVO=new StatisticsNumberVO();
        Integer confirmed = 0;
        Integer deliveryInProgress = 0;
        Integer toBeConfirmed = 0;
        for (Orders orders : list) {
             if (orders.getStatus()==Orders.DELIVERY_IN_PROGRESS)
             {
                 deliveryInProgress++;
             }
             if (orders.getStatus()==Orders.PENDING_ORDERS)
             {
                 toBeConfirmed++;
             }
            if (orders.getStatus()==Orders.RECEIVED_ORDER)
            {
                confirmed++;
            }
        }
        statisticsNumberVO.setConfirmed(confirmed);
        statisticsNumberVO.setToBeConfirmed(toBeConfirmed);
        statisticsNumberVO.setDeliveryInProgress(deliveryInProgress);
        return statisticsNumberVO;
    }

    /**
     * 确认订单。
     * 根据提供的订单ID，将订单的状态进行更新。
     *
     * @param id 订单的唯一标识符，类型为Long。
     * @return 无返回值。
     */
    @Override
    public void confirm(Long id) {
        // 根据ID更新订单状态
        Integer status=Orders.RECEIVED_ORDER;
        ordersMapper.updateById(id,status);
    }

    /**
     * 修改订单状态为配送中。
     *
     * @param id 订单的唯一标识符。
     * 通过调用ordersMapper的updateById方法，将指定订单的状态更新为DELIVERY_IN_PROGRESS。
     */
    @Override
    public void deliver(Long id) {
        // 更新订单状态为配送中
        ordersMapper.updateById(id, Orders.DELIVERY_IN_PROGRESS);
    }

    /**
     * 完成订单操作，将订单状态更新为取消，并记录当前时间作为交付时间。
     *
     * @param id 订单的唯一标识符，用于指定需要完成的订单。
     */
    @Override
    public void complete(Long id) {
        // 创建一个新的订单对象，用于更新订单状态和交付时间
        Orders order=new Orders();
        order.setStatus(Orders.COMPLETED_ORDER); // 设置订单状态为完成
        order.setDeliveryTime(LocalDateTime.now()); // 设置当前时间作为订单的交付时间
        order.setId(id); // 设置订单的唯一标识符
        ordersMapper.updateComplete(order); // 调用mapper层，执行订单状态的更新操作
    }

    /**
     * 通过订单ID取消订单（管理员操作）。
     * @param cancelOrdersDTO 取消订单数据传输对象，包含订单ID、取消原因等信息。
     *                         其中，订单ID用于定位需要取消的订单，取消原因用于记录订单取消的原因。
     * 注：此方法不返回任何值，通过修改数据库中的订单状态来实现订单的取消。
     */
    @Override
    public void cancaelOrderByIdBecauseAdmin(CancelOrdersDTO cancelOrdersDTO) {
        // 创建一个新的订单对象，用于记录订单被取消的状态和信息
        Orders orders=new Orders();
        orders.setStatus(Orders.ORDER_CANCELLED); // 设置订单状态为已取消
        orders.setId(cancelOrdersDTO.getId()); // 设置需要取消的订单ID
        orders.setCancelTime(LocalDateTime.now()); // 记录取消时间
        orders.setCancelReason(cancelOrdersDTO.getCancelReason()); // 设置取消原因
        // 调用订单Mapper层，将订单状态更新为已取消
        ordersMapper.cancelOrderById(orders);
    }






}
