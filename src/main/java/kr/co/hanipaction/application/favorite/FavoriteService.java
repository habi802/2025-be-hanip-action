package kr.co.hanipaction.application.favorite;

import kr.co.hanipaction.application.favorite.model.FavoriteGetDto;
import kr.co.hanipaction.application.favorite.model.FavoriteGetRes;
import kr.co.hanipaction.application.favorite.model.FavoritePostReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {
    final FavoriteMapper favoriteMapper;

    public int save(FavoritePostReq req) {
        return favoriteMapper.save(req);
    }

    public List<FavoriteGetRes> findAll(long userId) {
        return favoriteMapper.findAllByUserId(userId);
    }

    public Integer find(long storeId, long userId) {
        FavoriteGetDto dto = FavoriteGetDto.builder()
                .storeId(storeId)
                .userId(userId)
                .build();
        return favoriteMapper.findByStoreIdAndUserId(dto);
    }

    public int delete(long userId, long storeId) {
        return favoriteMapper.deleteByUserIdAndStoreId(userId, storeId);
    }
}
