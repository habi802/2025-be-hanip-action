package kr.co.hanipaction.application.order.newmodel;

import kr.co.hanipaction.configuration.enumcode.model.OrdersType;
import kr.co.hanipaction.configuration.enumcode.model.StatusType;
import kr.co.hanipaction.entity.actor.StoreId;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderPostDto {
    private Long userId;
    private long storeId;
    private String postcode;
    private String address;
    private List<OrderItemsPostDto> items;
    private int amount;
    private OrdersType payment;
    private StatusType status;


}
