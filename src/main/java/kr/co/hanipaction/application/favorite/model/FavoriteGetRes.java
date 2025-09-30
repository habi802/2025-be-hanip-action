package kr.co.hanipaction.application.favorite.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FavoriteGetRes {
    private long id;
    private long storeId;
    private String name;
    private String imagePath;
    private int favorites;
    private Double rating;
    private int minAmount;
    private int mimDeliveryFee;
    private int maxDeliveryFee;
}