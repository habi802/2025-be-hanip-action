package kr.co.hanipaction.application.order.newmodel;

import kr.co.hanipaction.entity.OrdersMenu;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderGetRes {
    private Long orderId;
    private long storeId;
    private String storeName;
    private String storePic;
    private Double rating;
    private int favorites;
    private int minAmount;
    private LocalDateTime createAt;
    private List<OrdersMenu> menuItems = new ArrayList<>();


    @Getter
    @Setter
    public static class OrderMenuItemRes {
        private long menuId;
        private String name;
        private long price;
        private String imagePath;
        private List<OrderDetailGetRes.OrderMenuOptionRes> options = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class OrderMenuOptionRes {
        private long optionId;
        private String comment;
        private long price;
        private List<OrderDetailGetRes.OrderMenuOptionRes> children = new ArrayList<>();
    }

}
