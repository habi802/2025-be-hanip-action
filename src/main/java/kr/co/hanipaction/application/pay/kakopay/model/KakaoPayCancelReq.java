package kr.co.hanipaction.application.pay.kakopay.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayCancelReq {
    private String cid;
    private String tid;
    @JsonProperty("cancel_amount")
    private Integer cancelAmount;
    @JsonProperty("cancel_tax_free_amount")
    private Integer cancelTaxFreeAmount;
}