package kr.co.hanipaction.application.review.model.newModal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewGetByStroeIdOwner {
    private String reviewId;
    private String orderId;
    private String ownerComment;
}
