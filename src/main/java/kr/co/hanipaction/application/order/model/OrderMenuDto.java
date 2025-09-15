package kr.co.hanipaction.application.order.model;

import kr.co.hanipaction.application.sse.model.OrderMenuOptionDto;
import kr.co.hanipaction.entity.Orders;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class OrderMenuDto {
    private long orderId;
    private long menuId;
    private long quantity;
    private String menuName;
    private int amount;
    private List<OrderMenuOptionDto> option;
}
