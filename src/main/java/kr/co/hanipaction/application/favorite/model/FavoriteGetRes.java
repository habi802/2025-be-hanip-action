package kr.co.hanipaction.application.favorite.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class FavoriteGetRes {
    private int storeId;
    private String name;
    private String imagePath;
    private int favorites;
    private long rating;
    private int reviews;
}