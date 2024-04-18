package com.service;

import com.vo.SalesTop10ReportVO;

import java.time.LocalDate;

public interface OrderDetailService {
    SalesTop10ReportVO ordersTop(LocalDate begin, LocalDate end);
}
