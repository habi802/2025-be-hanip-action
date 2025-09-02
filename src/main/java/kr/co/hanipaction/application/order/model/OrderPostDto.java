package kr.co.hanipaction.application.order.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderPostDto {
    private long id;
    private long userId;
    private long storeId;
    private String address;
    private int amount;


}
