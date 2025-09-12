package kr.co.hanipaction.application.order.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

    public OrderGetReq(Integer page, Integer rowPerPage, String menuName, String storeName) {

        this.page = page;
        this.rowPerPage = rowPerPage;
        this.menuName = menuName;
        this.storeName = storeName;
    }


}
