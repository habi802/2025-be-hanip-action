package kr.co.hanipaction.openfeign.store.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StoreGetRes {
    private Long id;
    private int favorites;
    private Double rating;
    private int isOpen;
    private int isActive;
    private int isPickUp;
    private int maxDeliveryFee;
    private int minDeliveryFee;
    private String businessNumber;
    private String ownerName;
    private String tel;
    private String name;
    private String postCode;
    private String address;
    private String addressDetail;
    private String imagePath;
    private String licensePath;
    private String comment;
    private String eventComment;
    private String openTIme;
    private String closeTime;
    private String openDate;

}
