package kr.co.hanipaction.application.manager.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewListReq {
    private String startDate;
    private String endDate;
    private String userName;
    private String storeName;
    private String isHide;
    private String comment;
    private String ownerComment;
    private int pageNumber;
    private int pageSize;
}
