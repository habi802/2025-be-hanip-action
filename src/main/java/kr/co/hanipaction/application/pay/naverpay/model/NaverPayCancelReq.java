package kr.co.hanipaction.application.pay.naverpay.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverPayCancelReq {
    private String paymentId;
    private Number cancelAmount;
    private String cancelReason;
    private String cancelRequester;
    private Number taxScopeAmount;
    private Number taxExScopeAmount;

}
