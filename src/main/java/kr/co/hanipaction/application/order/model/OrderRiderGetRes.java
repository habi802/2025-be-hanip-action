package kr.co.hanipaction.application.order.model;

import kr.co.hanipaction.configuration.enumcode.model.StatusType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class OrderRiderGetRes {
    private long id;
    private String menu;
    private String address;
    private int amount;
    private String riderRequest;
    private StatusType status;
}
