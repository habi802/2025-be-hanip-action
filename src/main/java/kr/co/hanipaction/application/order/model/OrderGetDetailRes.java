package kr.co.hanipaction.application.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderGetDetailRes {
    private int id; // o.id
    private int storeId;
    private int userId; // o.user_id
    private String storeName; //s.`name`
    private String userName;
    private int amount; // o.amount
    private String status; // o.`status`
    private String address;
    private String postcode;
    private String addressDetail;
    private Date created;
    private Date updated;
    private String phone;
    private int isDeleted;
    private List<OrderMenuDto> menus; // 여러 메뉴
}
