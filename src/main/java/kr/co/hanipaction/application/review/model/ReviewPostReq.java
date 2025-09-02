package kr.co.hanipaction.application.review.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class ReviewPostReq {
    private long id;
    private long userId;
    private long orderId;
    private double rating;
    private String comment;
    private String imagePath;
}
