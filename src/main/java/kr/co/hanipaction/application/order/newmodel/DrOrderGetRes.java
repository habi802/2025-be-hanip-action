package kr.co.hanipaction.application.order.newmodel;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DrOrderGetRes {
    private long orderId;
    private long storeId;
    private String storeName;
    private String storeAddress;
    private String userAddress;
    private String userAddressDetail;
    private String menuName;
    private String menuLength;
    private int amount;
    private String storeRequest;

}
