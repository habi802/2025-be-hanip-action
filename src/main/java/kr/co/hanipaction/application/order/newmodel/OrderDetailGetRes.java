package kr.co.hanipaction.application.order.newmodel;

import kr.co.hanipaction.entity.OrdersMenu;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
public  class OrderDetailGetRes {
    private long orderId;
    private long storeId;
    private String storeName;
    private String storeImg;
    private String menuName;
    private String status;
    private String userPhone;
    private String userName;
    private String address;
    private String addressDetail;
    private String storeRequest;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String payment;
    private int amount;
    private int minDeliveryFee;

    private List<OrderMenuItemRes> menuItems = new ArrayList<>();

    @Getter
    @Setter
    public static class OrderMenuItemRes {
        private long menuId;
        private String name;
        private long price;
        private String imagePath;
        private long quantity;
        private List<OrderMenuOptionRes> options = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class OrderMenuOptionRes {
        private long optionId;
        private String comment;
        private long price;
        private List<OrderMenuOptionRes> children = new ArrayList<>();
    }
}
