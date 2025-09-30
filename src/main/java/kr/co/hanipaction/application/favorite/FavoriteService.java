package kr.co.hanipaction.application.favorite;

import jakarta.transaction.Transactional;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.favorite.model.*;
import kr.co.hanipaction.application.order.OrderRepository;
import kr.co.hanipaction.entity.Favorites;
import kr.co.hanipaction.entity.Orders;
import kr.co.hanipaction.openfeign.store.StoreClient;
import kr.co.hanipaction.openfeign.store.model.StoreGetRes;
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
    private final OrderRepository orderRepository;

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
    @Transactional
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
    @Transactional
    public List<FavoriteGetRes> findAll(Long userId) {
        List<FavoriteGetRes> favorite = favoriteMapper.findAllByUserId(userId);

        for (FavoriteGetRes favoriteGetRes :favorite) {


            ResultResponse<StoreGetRes> storeRes = storeClient.findStore(favoriteGetRes.getStoreId());
            StoreGetRes store =  storeRes.getResultData();
            favoriteGetRes.setMaxDeliveryFee(store.getMaxDeliveryFee());
            favoriteGetRes.setMinAmount(store.getMinAmount());
            favoriteGetRes.setMimDeliveryFee(store.getMinDeliveryFee());
            favoriteGetRes.setFavorites(store.getFavorites());
            favoriteGetRes.setStoreId(store.getId());
            favoriteGetRes.setImagePath(store.getImagePath());
            favoriteGetRes.setRating(store.getRating());
            favoriteGetRes.setName(store.getName());
        }



//        return favoriteMapper.findAllByUserId(userId);
    return  favorite;
    }

    // ?
    @Transactional
    public FavoriteGetUser find(long userId,Long storeId) {
        FavoriteGetDto dto = FavoriteGetDto.builder()
                .userId(userId)
                .storeId(storeId)
                .build();

        FavoriteGetCheck on = favoriteMapper.findByStoreIdAndUserId(dto);
        FavoriteGetUser info = new FavoriteGetUser();

        if(on == null) {

            info.setOn(0);
        }else {

            info.setOn(1);
        }


        return  info;
    }

    // 찜 삭제
    @Transactional
    public void delete(Long userId, Long storeId) {
        favoriteMapper.deleteByUserIdAndStoreId(userId, storeId);

        // 찜 삭제 후 총 찜 수 계산한 뒤 Action의 Store로 전달
        patchSumFavorite(storeId);
    }

    // 유저 좋아요 유무
    @Transactional
    public boolean getStoreFavorites(Long storeId, Long userId) {
        boolean favorite = favoriteMapper.findIsFavoriteByStoreIdAndUserId(storeId, userId);
        return favorite;
    }
}
