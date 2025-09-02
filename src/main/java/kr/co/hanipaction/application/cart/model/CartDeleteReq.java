package kr.co.hanipaction.application.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@AllArgsConstructor

public class CartDeleteReq {
    private int cartId;
    private long userId;

}
