package kr.co.hanipaction.configuration.constants;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "constants.pay.naver.data")
@RequiredArgsConstructor
public class ConstNaverPay {
    public final String cid;
    public final String clientId;
    public final String clientSecret;
    public final String chainId;
    public final String returnUrl;
}
