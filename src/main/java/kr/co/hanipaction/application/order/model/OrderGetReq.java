package kr.co.hanipaction.application.order.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.co.hanipaction.application.order.newmodel.OrderGetDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@ToString
public class OrderGetReq {
    @NotNull(message = "page값은 필수입니다.")
    @Positive
    private Integer page;

    @Min(value = 1, message = "1이상")
    @Max(value = 6, message = "6이하")
    @NotNull(message = "row_per_page값은 필수입니다.")
    private Integer rowPerPage;

    private String menuName;
    private String storeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private OrderGetDto.PeriodType periodType;

    public OrderGetReq(Integer page, Integer rowPerPage, String menuName, String storeName,  LocalDate startDate, LocalDate endDate, OrderGetDto.PeriodType periodType) {

        this.page = page;
        this.rowPerPage = rowPerPage;
        this.menuName = menuName;
        this.storeName = storeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodType = periodType;
    }


}
