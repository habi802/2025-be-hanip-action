package kr.co.hanipaction.application.pay.kakopay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayApproveRes {

    private String aid; // 승인/ 취소 구분 번호
    private String tid;
    private String cid;
    private String sid;
    @JsonProperty("partner_order_id")
    private String partnerOrderId;
    @JsonProperty("partner_user_id")
    private String partnerUserId;
    @JsonProperty("payment_method_type")
    private String paymentMethodType; // 결제수단
    private KakaoPayAmountJson amount;
    private String item_name;
    private String item_code;
    private Integer quantity;
    @JsonProperty("createAt")
    private String createdAt;
    @JsonProperty("approved_at")
    private String approvedAt;
    private String payload;



}
