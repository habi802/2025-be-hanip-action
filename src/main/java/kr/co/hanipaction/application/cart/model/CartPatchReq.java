package kr.co.hanipaction.application.cart.model;

import lombok.Getter;

import java.util.List;

@Getter
public class CartPatchReq {
    private int quantity;
    private List<Long> optionId;
}
