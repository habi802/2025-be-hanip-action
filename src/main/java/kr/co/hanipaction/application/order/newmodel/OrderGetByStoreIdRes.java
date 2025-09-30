package kr.co.hanipaction.application.order.newmodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderGetByStoreIdRes {
    private long orderId;
    private long storeId;
    private String status;
    private int amount;
    private String createdAt;
}
