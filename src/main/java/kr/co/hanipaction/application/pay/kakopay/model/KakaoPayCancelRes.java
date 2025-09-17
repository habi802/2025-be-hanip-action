package kr.co.hanipaction.application.pay.kakopay.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayCancelRes {
    private String aid;
    private String tid;
    private String cid;
    private String status;
    @JsonProperty("partner_order_id")
    private String partnerOrderId;
    @JsonProperty("partner_user_id")
    private String partnerUserId;
    @JsonProperty("payment_method_type")
    private String paymentMethodType;
    private KakaoPayAmountJson amount;
    @JsonProperty("approved_cancel_amount")
    private KakaoPayApprovedCancelAmountJson approvedCancelAmount;
    @JsonProperty("canceled_amount")
    private KakaoPayCanceledAmountJson canceledAmount;
    @JsonProperty("cancel_available_amount")
    private KakaoPayCancelAvailableAmountJson cancelAvailableAmount;
    @JsonProperty("item_name")
    private  String itemName;
    @JsonProperty("item_code")
    private  String itemCode;
    private Integer quantity;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("approved_at")
    private String approvedAt;
    @JsonProperty("canceled_at")
    private String canceledAt;
    private String payload;
}
