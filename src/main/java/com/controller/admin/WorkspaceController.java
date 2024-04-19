package com.controller.admin;

import com.result.Result;
import com.service.WorkspaceService;
import com.vo.BusinessDataVO;
import com.vo.DishOverViewVO;
import com.vo.OrderOverViewVO;
import com.vo.SetmealOverViewVO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/admin/workspace")
@Slf4j
public class WorkspaceController {
    @Autowired
  private WorkspaceService workspaceService;

    /**
     * 查询今日运营数据
     * <p>
     * 该接口无需参数，返回今日的运营数据概况。
     *
     * @return Result<BusinessDataVO> 返回运营数据的结果，包含运营数据的详细信息。
     */
    @GetMapping("/businessData")
    @ApiOperation("查询今日运营数据")
    public Result<BusinessDataVO> businessData()
    {
        log.info("查询今日营业数据"); // 记录查询开始的日志
        BusinessDataVO businessDataVO=new BusinessDataVO(); // 初始化运营数据视图对象
        businessDataVO=workspaceService.businessData(); // 从服务层获取今日运营数据
        return Result.success(businessDataVO); // 返回成功响应，包含运营数据
    }
    /**
     * 查询订单管理数据的接口
     *
     * 本接口无需参数，用于获取订单管理的概览数据。
     *
     * @return Result<OrderOverViewVO> 返回订单概览数据的结果对象，其中包含订单概览信息。
     */
    @GetMapping("/overviewOrders")
    @ApiOperation("查询订单管理数据")
    public Result<OrderOverViewVO> overviewOrders()
    {
        log.info("查询订单管理数据");
        // 创建订单概览VO实例
        OrderOverViewVO overViewVO=new OrderOverViewVO();
        // 调用服务层方法，获取并设置订单概览数据
        overViewVO=workspaceService.overviewOrders();
          // 返回成功结果，包含订单概览数据
          return Result.success(overViewVO);
    }
    /**
     * 查询菜品总览
     *
     * 本接口不需要接收任何参数，返回餐厅菜品的总览信息。
     *
     * @return Result<DishOverViewVO> 返回一个结果对象，其中包含菜品总览信息。
     */
    @GetMapping("/overviewDishes")
    @ApiOperation("查询菜品总览")
    public Result<DishOverViewVO> overviewDishes()
    {
        log.info("查询菜品总览");
        // 创建菜品总览VO实例
        DishOverViewVO dishOverViewVO=new DishOverViewVO();
        // 通过服务层获取菜品总览数据
        dishOverViewVO=workspaceService.overviewDishes();
        // 返回成功结果，包含菜品总览信息
        return Result.success(dishOverViewVO);
    }
    /**
     * 查询套餐总览
     * 该接口不接受任何参数，返回当前系统中套餐的总览信息。
     *
     * @return Result<SetmealOverViewVO> 返回套餐总览信息的结果对象，其中包含套餐总览的详细信息。
     */
    @GetMapping("/overviewSetmeals")
    @ApiOperation("查询套餐总览")
    public Result<SetmealOverViewVO> overviewSetmeals()
    {
        log.info("查询套餐总览");
        // 创建套餐总览VO实例
        SetmealOverViewVO setmealOverViewVO=new SetmealOverViewVO();
        // 通过服务层获取套餐总览信息并赋值
        setmealOverViewVO=workspaceService.overviewSetmeals();
        // 返回成功结果，包含套餐总览信息
        return Result.success(setmealOverViewVO);
    }

}
