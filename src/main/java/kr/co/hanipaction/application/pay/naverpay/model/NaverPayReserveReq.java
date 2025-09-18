package kr.co.hanipaction.application.pay.naverpay.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NaverPayReserveReq {

    private String merchantPayKey;
    private String productName;
    private Number productCount;
    private Number totalPayAmount;
    private Number taxScopeAmount;
    private Number taxExScopeAmount;
    private String returnUrl;
    private List<NaverProductItem> productItems;
}
