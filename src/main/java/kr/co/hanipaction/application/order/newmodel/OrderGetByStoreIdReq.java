package kr.co.hanipaction.application.order.newmodel;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrderGetByStoreIdReq {

    private long storeId;
    private LocalDate startDate;
    private LocalDate endDate;
    private OrderGetByStoreIdDto.PeriodType periodType;

    public OrderGetByStoreIdReq (long storeId,LocalDate startDate, LocalDate endDate, OrderGetByStoreIdDto.PeriodType periodType) {
        this.storeId= storeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodType = periodType;
    }
}
