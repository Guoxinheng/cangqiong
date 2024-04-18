package com.mapper;

import com.entity.Orders;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrdersMapper {
    //保存订单
    void save(Orders order);

    /**
     * 根据订单号和用户id查询订单
     * @param orderNumber
     * @param userId
     */
    @Select("select * from orders where number = #{orderNumber} and user_id= #{userId}")
    Orders getByNumberAndUserId(@Param("orderNumber") String orderNumber,@Param("userId") Long userId);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);


    @Update("update orders set status = #{orderStatus},pay_status = #{orderPaidStatus} ,checkout_time = #{check_out_time} where number = #{id}")
    void updateStatus(@Param("orderStatus") Integer orderStatus,@Param("orderPaidStatus") Integer orderPaidStatus,@Param("check_out_time") LocalDateTime check_out_time,@Param("id") Long id);


    /**
     * 处理超时订单的函数。
     *
     * @param pendingPayment 订单取消状态，1代表订单已取消，其他值代表订单未取消。
     * @param minusMinutes  从当前时间向过去计算的分钟数，用于查找超时订单。
     * @return 返回一个订单列表，包含所有满足超时条件的订单。
     */
    @Select("select * from orders where status=#{pendingPayment} and order_time<#{minusMinutes}")
    List<Orders> processTimeOutOrderAndCancelOrder(@Param("pendingPayment") Integer pendingPayment,@Param("minusMinutes") LocalDateTime minusMinutes);
    /**
     * 处理取消订单的请求。
     *
     * @param deliveryInProgress 一个整数，代表正在进行配送的订单状态。具体值的含义根据业务逻辑定义。
     * @return 返回一个订单列表，这些订单代表了被取消的订单信息。
     */

    @Select("select * from orders where status=#{deliveryInProgress}")
    List<Orders> processCancelOrder(@Param("deliveryInProgress") Integer deliveryInProgress);
    /**
     * 获取用户的历史订单
     * @param status 订单的状态，用于筛选订单
     * @param userId 用户的ID，用于指定获取哪个用户的历史订单
     * @return 返回一个Page<Orders>对象，包含符合条件的订单列表，以及分页信息
     */
    Page<Orders> getHistoryOrders(@Param("status") Integer status,@Param("userId") Long userId);
    /**
     * 通过ID获取订单信息
     *
     * @param id 订单的唯一标识符
     * @return 返回对应ID的订单对象，如果不存在，则返回null。
     */
     @Select("select * from orders where id = #{id}")
     Orders getById(Long id);

    /**
     * 根据订单ID取消订单。
     * 该方法通过更新订单表中的状态、取消时间及取消原因字段来实现订单的取消。
     *
     * @param orders 包含订单ID、新的状态、取消时间和取消原因的订单对象。
     *               其中，订单ID用于定位需要取消的订单，
     *               状态用于更新订单的状态为已取消，
     *               取消时间和取消原因用于记录订单取消的时间和原因。
     * @return 该方法没有返回值。
     */
    @Update("update orders set status = #{status},cancel_time=#{cancelTime},cancel_reason=#{cancelReason} where id = #{id}")
    void cancelOrderById(Orders orders);

    /**
     * 条件搜索订单
     *
     * @param orders 包含搜索条件的订单对象
     * @return 返回符合条件的订单页面信息
     */
    /**
     * 条件搜索订单
     *
     * @param orders 包含搜索条件的订单对象
     * @return 返回符合条件的订单页面对象
     */
    Page<Orders> conditionSearch(Orders orders);

    /**
     * 更新订单状态为拒绝，并设置拒绝原因和取消时间
     *
     * @param orders 包含订单ID、新状态、拒绝原因和取消时间的订单对象
     */
    @Update("update orders set status=#{status},rejection_reason=#{rejectionReason},cancel_time=#{cancelTime} where id=#{id}")
    void rejection(Orders orders);
    /**
     * 从数据库中获取所有订单的信息列表。
     *
     * @return List<Orders> 返回一个订单信息的列表，包含所有订单。
     */
    @Select("select * from orders")
    List<Orders> getOrdersList();
    /**
     * 根据ID更新订单的状态。
     *
     * @param id 订单的唯一标识符，用于指定要更新的订单。
     * @return void 方法没有返回值。
     */
    @Update("update orders set status = #{status} where id = #{id}")
    void updateById(@Param("id") Long id,@Param("status") Integer status);

    /**
     * 更新订单状态为完成。
     * @param order 需要更新状态的订单对象。
     * 该方法不返回任何内容。
     */
    void updateComplete(Orders order);

}
