package kr.co.hanipaction.application.favorite;

import kr.co.hanipaction.application.favorite.model.FavoriteGetDto;
import kr.co.hanipaction.application.favorite.model.FavoriteGetRes;
import kr.co.hanipaction.application.favorite.model.FavoritePostReq;
import kr.co.hanipaction.entity.Favorites;
import kr.co.hanipaction.entity.FavoritesIds;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {
    final FavoriteMapper favoriteMapper;
    final FavoriteRepository favoriteRepository;

    public int save(Long signedUserId, FavoritePostReq req) {
        // req에서 userId와 storeId를 가져와서 Favorite 엔티티 객체 생성

        Favorites favorites = Favorites.builder()
                .userId(signedUserId)
                .storeId(req.getStoreId())
                .build();


        favoriteRepository.save(favorites);

        return 1;
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
