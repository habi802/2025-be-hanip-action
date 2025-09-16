package kr.co.hanipaction.application.pay.kakopay.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayReadyReq {
    private String cid;
    @JsonProperty("partner_order_id")
    private String partnerOrderId;
    @JsonProperty("partner_user_id")
    private String partnerUserId;
    @JsonProperty("item_name")
    private String itemName;
    private Integer quantity;
    @JsonProperty("total_amount")
    private Integer totalAmount;
    @JsonProperty("tax_free_amount")
    private Integer taxFreeAmount;
    @JsonProperty("approval_url")
    private String approvalUrl;
    @JsonProperty("cancel_url")
    private String cancelUrl;
    @JsonProperty("fail_url")
    private String failUrl;


}
