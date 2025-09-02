package kr.co.hanipaction.application.review.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ReviewGetReq {
    private int id;
    private int userId;
    private int storeId;
    private String userName;
    private double rating;
    private String menuName;
    private int menuCount;
    private String imagePath;
    private String comment;
    private String ownerComment;
    private String created;
}
