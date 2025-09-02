package kr.co.hanipaction.application.order.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OrderStatusPatchReq {
    private String orderId;
    private String status;
}
