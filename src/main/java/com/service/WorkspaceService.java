package com.service;

import com.vo.BusinessDataVO;
import com.vo.DishOverViewVO;
import com.vo.OrderOverViewVO;
import com.vo.SetmealOverViewVO;

import java.time.LocalDate;

public interface  WorkspaceService {
    BusinessDataVO businessData();

    OrderOverViewVO overviewOrders();

    DishOverViewVO overviewDishes();

    SetmealOverViewVO overviewSetmeals();
     BusinessDataVO businessData(LocalDate begin1, LocalDate end1);
}
