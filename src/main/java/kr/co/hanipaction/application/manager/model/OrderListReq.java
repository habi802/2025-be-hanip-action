package kr.co.hanipaction.application.manager.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class OrderListReq {
    private String startDate;
    private String endDate;
    private String storeName;
    private String userName;
    private String address;
    private String payment;
    private String isDeleted;
    private String status;
    private int pageNumber;
    private int pageSize;
}
