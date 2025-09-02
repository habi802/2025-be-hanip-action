package kr.co.hanipaction.application.order.model;

import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class OrderPostReq {
    private int storeId;
    private String address;
    private List<OrderMenuVo> orders;

}
