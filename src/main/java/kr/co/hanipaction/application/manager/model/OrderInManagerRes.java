package kr.co.hanipaction.application.manager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class OrderInManagerRes {
    private long orderId;
    private String storeName;
    private String userName;
    private String address;
    private int amount;
    private String createdAt;
    private String payment;
    private String status;
    private int isDeleted;
}
