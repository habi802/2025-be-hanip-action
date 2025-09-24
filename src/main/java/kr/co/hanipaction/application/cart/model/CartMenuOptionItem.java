package kr.co.hanipaction.application.cart.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartMenuOptionItem {
    private long menuId;
    private long optionId;
    private int quantity;

}
