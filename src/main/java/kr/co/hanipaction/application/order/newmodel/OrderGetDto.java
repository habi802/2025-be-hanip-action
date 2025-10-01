package kr.co.hanipaction.application.order.newmodel;


import lombok.*;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderGetDto {
    private long userId;
    private String searchText;
    private int startIdx;
    private int size;
    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType periodType;

    public enum PeriodType {
        WEEK, MONTH1, MONTH3
    }
}
