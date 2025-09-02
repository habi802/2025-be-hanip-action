package kr.co.hanipaction.application.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class OrderGetListReq {
    private int orderId;
    private int menuId;
    private String name;
    private int quantity;
    private int price;
}
