package kr.co.hanipaction.application.pay.naverpay.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NaverPayCancelRes {
    private String code;
    private String message;
    private Object body;
}
