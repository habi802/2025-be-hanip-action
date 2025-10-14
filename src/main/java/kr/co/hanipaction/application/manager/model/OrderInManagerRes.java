package kr.co.hanipaction.application.manager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@ToString
public class OrderInManagerRes {
    private long orderId;
    private long storeId;
    private String createdAt;
    private String storeName;
    private String userName;
    private String address;
    private int amount;
    private String payment;
    private String status;
    private int isDeleted;
    private List<OrderInManagerRes.OrderMenuItemRes> menus = new ArrayList<>();

    @Getter
    @Setter
    public static class OrderMenuItemRes {
        private long menuId;
        private String name;
        private long price;
        private String imagePath;
        private long quantity;
        private List<OrderInManagerRes.OrderMenuOptionRes> options = new ArrayList<>();
    }

    @Getter
    @Setter
    public static class OrderMenuOptionRes {
        private long optionId;
        private String comment;
        private long price;
        private List<OrderInManagerRes.OrderMenuOptionRes> children = new ArrayList<>();
    }
}
