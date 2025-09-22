package kr.co.hanipaction.application.pay.naverpay.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverPayOrderItemReq {
        private int menuQuantity;
        private int menuPrice;
        private int totalPrice;
        private String manuName;
        private Long menuId;
        private Long userId;



}
