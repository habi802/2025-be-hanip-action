package kr.co.hanipaction.application.manager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@Builder
@ToString
public class ReviewInManagerRes {
    private long reviewId;
    private String storeName;
    private String userName;
    private List<String> images;
    private String comment;
    private String ownerComment;
    private int isHide;
    private String createdAt;
}
