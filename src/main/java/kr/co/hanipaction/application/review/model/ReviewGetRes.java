package kr.co.hanipaction.application.review.model;

import kr.co.hanipaction.entity.Orders;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewGetRes {
    private long id;
    private long orderId;
    private long storeId;
    private long userId;
    private String userName;
    private String userPic;
    private double rating;
    private List<String> menuName;
    private int menuCount;
    private String comment;
    private String ownerComment;
    private String createdAt;
    private int isHide;
    private List<String> pic;


}
