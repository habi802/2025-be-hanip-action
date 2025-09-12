package kr.co.hanipaction.application.order.newmodel;

import kr.co.hanipaction.entity.OrdersMenu;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderGetRes {
    private Long orderId;
    private long storeId;
    private String storeName;
    private String menuName;
    private String storePic;
    private Double rating;
    private int favorites;
    private int minAmount;
    private LocalDateTime createAt;


    private List<OrdersMenu> menuItems = new ArrayList<>();
}
