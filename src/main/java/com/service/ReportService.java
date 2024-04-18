package com.service;

import com.dto.OrderReportVO;
import com.vo.TurnoverReportVO;
import com.vo.UserReportVO;

import java.time.LocalDate;

public interface ReportService {
    TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end);

    UserReportVO userStatistics(LocalDate begin, LocalDate end);

    OrderReportVO ordersStatistics(LocalDate begin, LocalDate end);
}
