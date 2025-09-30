package kr.co.hanipaction.application.order.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class OrderStatusDto {
    private Long userId;
    private Long storeId;
    private int startIdx;
    private int size;
    private String startDate;
    private String endDate;
    private String searchType;
    private String keyword;
}
