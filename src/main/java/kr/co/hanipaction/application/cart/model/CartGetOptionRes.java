package kr.co.hanipaction.application.cart.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartGetOptionRes {
    private long menuId;
    private List<Long> optionId = new ArrayList<>();
    private int quantity;
}
