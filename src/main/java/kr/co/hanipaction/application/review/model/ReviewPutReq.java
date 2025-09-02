package kr.co.hanipaction.application.review.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewPutReq {
    private long id;
    private long userId;
    private int rating;
    private String comment;
    private String imagePath;
}
