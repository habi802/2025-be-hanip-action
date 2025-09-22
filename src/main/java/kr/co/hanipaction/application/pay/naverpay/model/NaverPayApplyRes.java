package kr.co.hanipaction.application.pay.naverpay.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NaverPayApplyRes {
    private String code;
    private String message;
    private Object body;
}
