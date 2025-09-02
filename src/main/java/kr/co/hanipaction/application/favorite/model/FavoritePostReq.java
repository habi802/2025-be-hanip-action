package kr.co.hanipaction.application.favorite.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FavoritePostReq {
    private long userId;
    private int storeId;
}
