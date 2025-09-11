package kr.co.hanipaction.application.order.newmodel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderGetRes {
    private Long orderId;
    private String storeId;
    private String storeName;
    private Double rating;
    private int favorites;
    private int minAmount;
}
