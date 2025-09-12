package kr.co.hanipaction.application.order.newmodel;

import kr.co.hanipaction.entity.OrdersMenu;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderDetailGetRes {
    private long orderId;
    private long storeId;
    private String storeName;
    private long menuId;
    private String menuName;
    private String status;





    private List<OrdersMenu> menuItems = new ArrayList<>();
}
