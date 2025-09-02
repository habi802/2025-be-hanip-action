package kr.co.hanipaction.application.review.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
public class ReviewPatchReq {
    private int reviewId;
    private String ownerComment;
}
