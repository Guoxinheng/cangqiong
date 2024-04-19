package com.service.impl;

import com.entity.Orders;
import com.mapper.DishMapper;
import com.mapper.OrdersMapper;
import com.mapper.SetmealMapper;
import com.mapper.UserMapper;
import com.service.WorkspaceService;
import com.vo.BusinessDataVO;
import com.vo.DishOverViewVO;
import com.vo.OrderOverViewVO;
import com.vo.SetmealOverViewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {
    @Autowired
   private UserMapper userMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    /**
     * 获取今日的业务数据统计信息
     * 本方法不接受参数，返回包含今日新增用户数、订单完成率、营业额、有效订单数和单位价格的BusinessDataVO对象。
     *
     * @return BusinessDataVO 一个包含今日业务统计数据的VO对象，包括新增用户数、订单完成率、营业额、有效订单数和单位价格。
     */
    @Override
    public BusinessDataVO businessData() {
        // 计算今日的时间范围，从凌晨0点到晚上11点59分59秒
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime begin = now.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime end = now.withHour(23).withMinute(59).withSecond(59);

        // 查询今日新增用户数
        Long newUsers= userMapper.selectCountByTime(begin,end);
        newUsers=newUsers==null?0:newUsers;

        // 计算今日营业额
        Double turnover=0.0;
        turnover=ordersMapper.getTurnover(begin,end,Orders.COMPLETED_ORDER);
        turnover=turnover==null?0:turnover;
        // 查询今日客户数，即有过购买行为的用户数
        Double validUser=ordersMapper.getValidUser(begin,end,Orders.COMPLETED_ORDER);
        validUser=validUser==null? 0:validUser;

        // 查询今日有效订单数，即状态为已完成的订单数
        Double validOrderCount=ordersMapper.getValidOrderCount(begin,end,Orders.COMPLETED_ORDER);

        // 查询今日订单总数
        Double orderCount=ordersMapper.getOrderCount(begin,end);
        orderCount=orderCount==null?0:orderCount;

        // 计算今日订单完成率
        Double orderCompletionRate=0.0;
        if(orderCount!=0){
            orderCompletionRate=validOrderCount/orderCount;
        }

        // 计算单位价格，即平均每位用户消费金额
        Double unitPrice=0.0;
        if (validUser!=0)
        {
            unitPrice =turnover/validUser;
        }

        // 将统计数据封装进BusinessDataVO对象并返回
        BusinessDataVO businessDataVO=new BusinessDataVO();
        businessDataVO.setNewUsers(newUsers);
        businessDataVO.setOrderCompletionRate(orderCompletionRate);
        businessDataVO.setTurnover(turnover);
        businessDataVO.setValidOrderCount(validOrderCount);
        businessDataVO.setUnitPrice(unitPrice);
        return businessDataVO;
    }

    /**
     * 获取订单概览信息
     * 该方法不接受任何参数，返回当前日期的订单概览信息，包括完成订单数、取消订单数、待派送订单数、待接单订单数和总订单数。
     *
     * @return OrderOverViewVO 订单概览信息对象
     */
    @Override
    public OrderOverViewVO overviewOrders() {
        OrderOverViewVO orderOverViewVO=new OrderOverViewVO();
        LocalDateTime begin=null;
        LocalDateTime end=null;

        // 查询并计算今日完成订单总数
        Double sumCompleteAmountByDate = ordersMapper.getSumByDate(begin, end, Orders.COMPLETED_ORDER);
        sumCompleteAmountByDate=sumCompleteAmountByDate==null?0:sumCompleteAmountByDate;

        // 查询并计算今日取消订单总数
        Double sumCancelAmountByDate = ordersMapper.getSumByDate(begin, end, Orders.ORDER_CANCELLED);
        sumCancelAmountByDate=sumCancelAmountByDate==null?0:sumCancelAmountByDate;

        // 查询并计算今日待派送订单总数
        Double sumReceivedOrderAmountByDate = ordersMapper.getSumByDate(begin, end, Orders.RECEIVED_ORDER);
        sumReceivedOrderAmountByDate=sumReceivedOrderAmountByDate==null?0:sumReceivedOrderAmountByDate;

        // 查询并计算今日待接单订单总数
        Double sumPendingOrdersAmountByDate = ordersMapper.getSumByDate(begin, end, Orders.PENDING_ORDERS);
        sumPendingOrdersAmountByDate=sumPendingOrdersAmountByDate==null?0:sumPendingOrdersAmountByDate;

        // 查询并计算今日全部订单总数
        Double orderCount = ordersMapper.getOrderCount(begin, end);
        orderCount=orderCount==null?0:orderCount;

        // 设置订单概览信息对象的各个属性
        orderOverViewVO.setAllOrders(orderCount);
        orderOverViewVO.setCancelledOrders(sumCancelAmountByDate);
        orderOverViewVO.setCompletedOrders(sumCompleteAmountByDate);
        orderOverViewVO.setDeliveredOrders(sumReceivedOrderAmountByDate);
        orderOverViewVO.setWaitingOrders(sumPendingOrdersAmountByDate);

        return orderOverViewVO;
    }

    /**
     * 获取菜品的概览信息
     * 该方法不接受任何参数
     *
     * @return DishOverViewVO 菜品概览信息对象，包含起售中的菜品数量和已停售的菜品数量
     */
    @Override
    public DishOverViewVO overviewDishes() {
        DishOverViewVO dishOverViewVO=new DishOverViewVO();
        // 查询起售中的菜品的数量
        Long dishStart = dishMapper.selectCountByStatus(1);
        // 查询已经停售的菜品数量
        Long dishStop =dishMapper.selectCountByStatus(0);

        // 处理查询结果可能为null的情况，确保数据的准确性
        dishStart=dishStart==null?0:dishStart;
        dishStop=dishStop==null?0:dishStop;

        // 设置起售中和已停售的菜品数量到返回对象中
        dishOverViewVO.setDiscontinued(dishStop);
        dishOverViewVO.setSold(dishStart);

        return dishOverViewVO;
    }

    /**
     * 获取套餐概览信息
     * 该方法不接受任何参数，返回一个包含起售套餐和停售套餐数量的套餐概览视图对象。
     *
     * @return SetmealOverViewVO 套餐概览视图对象，其中包含停售套餐数量和起售套餐数量。
     */
    @Override
    public SetmealOverViewVO overviewSetmeals() {
        SetmealOverViewVO setmealOverViewVO=new SetmealOverViewVO();
        // 查询起售套餐数量
    Long setmealStart=setmealMapper.selectCountByStatus(1);
    setmealStart=setmealStart==null?0:setmealStart;
        // 查询停售套餐数量
    Long setmealStop=setmealMapper.selectCountByStatus(0);
    setmealStop=setmealStop==null?0:setmealStop;
        // 设置概览对象中的停售套餐数量和起售套餐数量
        setmealOverViewVO.setDiscontinued(setmealStop);
        setmealOverViewVO.setSold(setmealStart);
        return setmealOverViewVO;
    }


    public BusinessDataVO businessData(LocalDate begin1, LocalDate end1) {
        // 计算今日的时间范围，从凌晨0点到晚上11点59分59秒
        LocalDateTime begin = LocalDateTime.of(begin1, LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(end1, LocalTime.MAX);

        // 查询今日新增用户数
        Long newUsers= userMapper.selectCountByTime(begin,end);
        newUsers=newUsers==null?0:newUsers;

        // 计算今日营业额
        Double turnover=0.0;
        turnover=ordersMapper.getTurnover(begin,end,Orders.COMPLETED_ORDER);
        turnover=turnover==null?0:turnover;
        // 查询今日客户数，即有过购买行为的用户数
        Double validUser=ordersMapper.getValidUser(begin,end,Orders.COMPLETED_ORDER);
        validUser=validUser==null? 0:validUser;

        // 查询今日有效订单数，即状态为已完成的订单数
        Double validOrderCount=ordersMapper.getValidOrderCount(begin,end,Orders.COMPLETED_ORDER);

        // 查询今日订单总数
        Double orderCount=ordersMapper.getOrderCount(begin,end);
        orderCount=orderCount==null?0:orderCount;

        // 计算今日订单完成率
        Double orderCompletionRate=0.0;
        if(orderCount!=0){
            orderCompletionRate=validOrderCount/orderCount;
        }

        // 计算单位价格，即平均每位用户消费金额
        Double unitPrice=0.0;
        if (validUser!=0)
        {
            unitPrice =turnover/validUser;
        }

        // 将统计数据封装进BusinessDataVO对象并返回
        BusinessDataVO businessDataVO=new BusinessDataVO();
        businessDataVO.setNewUsers(newUsers);
        businessDataVO.setOrderCompletionRate(orderCompletionRate);
        businessDataVO.setTurnover(turnover);
        businessDataVO.setValidOrderCount(validOrderCount);
        businessDataVO.setUnitPrice(unitPrice);
        return businessDataVO;
    }
}
