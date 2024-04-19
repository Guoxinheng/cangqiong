package com.constant.dto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
public class OrderSubmitDTO implements Serializable {
    Long addressBookId;//地址id
    BigDecimal amount;//总金额
    Integer deliveryStatus;//配送状态 1立即送出 0选择具体时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime estimatedDeliveryTime;//预计送达时间
    BigDecimal packAmount;//打包费
    Integer payMethod;//付款方式
    String remark;//备注
    Integer tablewareNumber;//餐具数量
    Integer tablewareStatus;//餐具数量状态 1按餐量提供 0选择具体数量
}
