package kr.co.hanipaction.application.favorite;

import kr.co.hanipaction.application.favorite.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FavoriteMapper {
    int save(FavoritePostReq req);
    List<FavoriteGetRes> findAllByUserId(long userId);
    FavoriteGetCheck findByStoreIdAndUserId(FavoriteGetDto dto);
    int deleteByUserIdAndStoreId(long userId, long storeId);
    boolean findIsFavoriteByStoreIdAndUserId(Long storeId, Long userId);

}
