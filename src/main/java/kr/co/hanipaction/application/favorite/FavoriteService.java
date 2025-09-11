package kr.co.hanipaction.application.favorite;

import kr.co.hanipaction.application.favorite.model.FavoriteGetDto;
import kr.co.hanipaction.application.favorite.model.FavoriteGetRes;
import kr.co.hanipaction.application.favorite.model.FavoritePostReq;
import kr.co.hanipaction.entity.Favorites;
import kr.co.hanipaction.entity.FavoritesIds;
import kr.co.hanipaction.openfeign.store.StoreClient;
import kr.co.hanipaction.openfeign.store.model.StorePatchReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {
    private final FavoriteMapper favoriteMapper;
    private final FavoriteRepository favoriteRepository;

    private final StoreClient storeClient;

    // 총 찜 수를 계산한 후, Store의 총 찜 수 컬럼을 수정할 수 있게 Action으로 전달
    private void patchSumFavorite(Long storeId) {
        if (storeId == null) {
            return;
        }

        Integer favorites = favoriteRepository.findSumFavoritesByStoreId(storeId);

        StorePatchReq storePatchReq = StorePatchReq.builder()
                .id(storeId)
                .favorites(favorites != null ? favorites : 0)
                .build();
        storeClient.patchStore(storePatchReq);
    }

    // 찜 등록
    public int save(Long signedUserId, FavoritePostReq req) {
        // req에서 userId와 storeId를 가져와서 Favorite 엔티티 객체 생성
        Favorites favorites = Favorites.builder()
                .userId(signedUserId)
                .storeId(req.getStoreId())
                .build();

        favoriteRepository.save(favorites);

        // 찜 등록 후 총 찜 수 계산한 뒤 Action의 Store로 전달
        patchSumFavorite(req.getStoreId());

        return 1;
    }

    // 자신의 찜 목록 조회
    public List<FavoriteGetRes> findAll(Long userId) {
        return favoriteMapper.findAllByUserId(userId);
    }

    // ?
    public Integer find(Long storeId) {
        FavoriteGetDto dto = FavoriteGetDto.builder()
                .storeId(storeId)
                .build();
        return favoriteMapper.findByStoreIdAndUserId(dto);
    }

    // 찜 삭제
    public void delete(Long userId, Long storeId) {
        favoriteMapper.deleteByUserIdAndStoreId(userId, storeId);

        // 찜 삭제 후 총 찜 수 계산한 뒤 Action의 Store로 전달
        patchSumFavorite(storeId);
    }
}
