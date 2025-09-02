package kr.co.hanipaction.application.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class OrderGetRes {
    private int id; // o.id
    private int userId; // o.user_id
    private int storeId;
    private String storeName; //s.`name`
    private int amount; // o.amount
    private String status; // o.`status`
    private Date created;
    private String imagePath;
    private int isDeleted;
    private List<OrderGetListReq> orderGetList;
}
