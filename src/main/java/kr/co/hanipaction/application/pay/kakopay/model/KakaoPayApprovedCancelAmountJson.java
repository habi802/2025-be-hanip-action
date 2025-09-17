package kr.co.hanipaction.application.pay.kakopay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KakaoPayApprovedCancelAmountJson {

    private Integer total;
    @JsonProperty("tax_free")
    private Integer taxFree;
    private Integer vat;
    private Integer point;
    private Integer discount;
    @JsonProperty("green_deposit")
    private Integer greenDeposit; // 컵 보증금??
}
