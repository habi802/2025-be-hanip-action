package kr.co.hanipaction.application.review.model;

import lombok.Getter;

import java.util.List;

@Getter
public class ReviewGetRes {
    private int id;
    private int orderId;
    private int storeId;
    private String userName;
    private double rating;
    private String menuName;
    private int menuCount;
    private String comment;
    private String ownerComment;
    private String created;
    private List<String> pic;
}
