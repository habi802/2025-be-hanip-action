package kr.co.hanipaction.application.pay.naverpay.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverProductItem {
    private String categoryType;
    private String categoryId;
    private String uid;
    private String name;
    private Number count;
}
