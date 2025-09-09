package kr.co.hanipaction.application.cart.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class CartPostReq {
    private long cartId;
    private long userId;
    private long menuId;
    private long storeId;
    private int amount;
    private List<Long> optionId;
    private int quantity;
}
