package kr.co.hanipaction.application.contact.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@ToString
public class ContactGetReq {
    @NotNull(message = "page값은 필수입니다.")
    @Positive
    private Integer page;

    @Min(value = 10, message = "10이상")
    @Max(value = 20, message = "20이하")
    @NotNull(message = "row_per_page값은 필수입니다.")
    private Integer rowPerPage;

    private Long userId;

    public ContactGetReq(Integer page
            , @BindParam("row_per_page") Integer rowPerPage
            , @BindParam("user_id") Long userId) {
        this.page = page;
        this.rowPerPage = rowPerPage;
        this.userId = userId;
    }
    public Long getUserId() {
        return userId == null ? 0 : userId;
    }
}
