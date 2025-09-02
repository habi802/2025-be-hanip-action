package kr.co.hanipaction.application.cart.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CartPostReq {
    private int cartId;
    private int userId;
    private int menuId;
    private int quantity;
}
