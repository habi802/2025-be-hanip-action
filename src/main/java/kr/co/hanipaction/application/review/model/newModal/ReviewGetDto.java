package kr.co.hanipaction.application.review.model.newModal;

import kr.co.hanipaction.application.order.newmodel.OrderGetDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewGetDto {
    private long id;
    private long orderId;
    private long userId;
    private double rating;
    private long storeId;
    private String comment;
    private String ownerComment;
    private String createdAt;
    private int isHide;

    private int startIdx;
    private int size;
    private LocalDate startDate;
    private LocalDate endDate;
    private OrderGetDto.PeriodType periodType;

    public enum PeriodType {
        WEEK, MONTH1, MONTH3
    }
}
