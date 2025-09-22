package kr.co.hanipaction.application.review.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ReviewPatchDto {
    private long reviewId;
    private String userComment;
    private String ownerComment;
}
