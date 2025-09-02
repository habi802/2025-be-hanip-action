package kr.co.hanipaction.application.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderMenuDto {
    private int orderId;
    private int menuId;
    private int quantity;
    private String name;
    private int price;
}
