package kr.co.hanipaction.application.order.newmodel;


import lombok.*;

import java.util.List;

@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderItemsPostDto {
    private long menuId;
    private int amount;
    private long quantity;
    private List<Long> optionId;

}
