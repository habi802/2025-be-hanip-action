package kr.co.hanipaction.application.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@ToString
public class OrderMenuPostDto {
    private long orderId;
    private List<OrderMenuVo> menuId;
}
