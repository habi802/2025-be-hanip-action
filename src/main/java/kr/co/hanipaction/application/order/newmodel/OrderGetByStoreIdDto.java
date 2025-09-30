package kr.co.hanipaction.application.order.newmodel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderGetByStoreIdDto {
    private long id;
    private long storeId;
    private String status;
    private int amount;
    private String createdAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private PeriodType periodType;

    public enum PeriodType {
        WEEK, MONTH1, YEAR1, YEAR2, YEAR3
    }
}
