package com.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TurnoverReportVO implements Serializable {
         String dateList;//日期列表,日期之间用逗号进行分割
       String  turnoverList;//营业额列表,营业额之间以逗号分割
}
