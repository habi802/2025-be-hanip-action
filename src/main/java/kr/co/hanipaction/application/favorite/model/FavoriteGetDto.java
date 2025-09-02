package kr.co.hanipaction.application.favorite.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteGetDto {
    private long storeId;
    private long userId;
}
