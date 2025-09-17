package kr.co.hanipaction.application.pay.kakopay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@Setter
public class KakaoPayPgTokenReq {
    private String pgToken;

    public KakaoPayPgTokenReq(@BindParam("pg_token") String pgToken) {
        this.pgToken = pgToken;
    }
}
