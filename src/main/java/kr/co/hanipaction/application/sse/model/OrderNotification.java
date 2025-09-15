package kr.co.hanipaction.application.sse.model;

import kr.co.hanipaction.application.order.model.OrderMenuDto;
import kr.co.hanipaction.configuration.enumcode.model.StatusType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderNotification {
    private Long orderId;
    private Long storeId;
    private Long userId;
    private StatusType status;
    private int amount;
    private List<OrderMenuDto> menus;
}
