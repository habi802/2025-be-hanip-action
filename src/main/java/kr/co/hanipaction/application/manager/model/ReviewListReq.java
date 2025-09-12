package kr.co.hanipaction.application.manager.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewListReq {
    private String startDate;
    private String endDate;
    private int pageNumber;
    private int pageSize;
}
