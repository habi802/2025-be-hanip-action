package kr.co.hanipaction.application.manager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ReviewListRes {
    private String createdAt;
    private long reviewId;
    private String userName;
    private String storeName;
    private String comment;
    private int ownerComment;
    private int isHide;
}
