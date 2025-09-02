package kr.co.hanipaction.application.cart.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartPatchDto {
    private int cartId;
    private long userId;
    private int quantity;
}
