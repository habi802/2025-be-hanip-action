package kr.co.hanipaction.application.cart.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString

public class CartListGetRes {
    private int id;
    private int menuId;
    private int storeId;
    private int quantity;
    private int price;
    private String name;
    private String imagePath;
}
