package kr.co.hanipaction.application.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.bind.annotation.BindParam;

@Getter
@Setter
@ToString
public class OrderStatusReq {
    private Long storeId;
    private Integer page;
    private Integer rowPerPage;
    private String startDate;
    private String endDate;
    private String searchType;
    private String keyword;

    public OrderStatusReq(Integer page
            , @BindParam("row_per_page") Integer rowPerPage
            , @BindParam("store_id") Long storeId
            , @BindParam("start_date") String startDate
            , @BindParam("end_date") String endDate
            , @BindParam("search_type") String searchType
            , @BindParam("keyword") String keyword) {
        this.page = page;
        this.rowPerPage = rowPerPage;
        this.storeId = storeId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.searchType = searchType;
        this.keyword = keyword;
    }
}
