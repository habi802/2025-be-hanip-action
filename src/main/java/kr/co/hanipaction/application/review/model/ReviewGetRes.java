package kr.co.hanipaction.application.review.model;

import kr.co.hanipaction.entity.Orders;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewGetRes {
    private long id;
    private long orderId;
    private long storeId;
    private String userName;
    private String userPic;
    private double rating;
    private String menuName;
    private int menuCount;
    private String comment;
    private String ownerComment;
    private String createdAt;
    private List<String> pic;
}
