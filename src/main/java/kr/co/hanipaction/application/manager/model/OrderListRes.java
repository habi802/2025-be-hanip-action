package kr.co.hanipaction.application.manager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class OrderListRes {
    private String createdAt;
    private long orderId;
    private String userName;
    private String storeName;
    private String address;
    private String payment;
    private String status;
    private int isDeleted;
}
