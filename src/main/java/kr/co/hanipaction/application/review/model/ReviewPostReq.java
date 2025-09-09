package kr.co.hanipaction.application.review.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString

public class ReviewPostReq {
    private long id;
    private long userId;

    @NotNull(message = "주문 번호는 필수입니다")
    private long orderId;
    private double rating;

    @NotNull(message = "코멘트는 필수입니다.")
    private String comment;
}
