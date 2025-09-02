package kr.co.hanipaction.application.review.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewPatchDto {
    private long reviewId;
    private long storeId;
}
