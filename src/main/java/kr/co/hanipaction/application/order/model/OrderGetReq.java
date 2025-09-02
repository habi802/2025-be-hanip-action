package kr.co.hanipaction.application.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class OrderGetReq {
    private int id; // o.id
    private int userId; // o.user_id
    private int storeId;
    private String storeName; //s.`name`
    private String menuName; // m.`name`
    private int quantity; //om.quantity
    private int price; //m.price
    private int amount; // o.amount
    private String status; // o.`status`
    private List<OrderGetListReq> orderGetList;

}
