package kr.co.hanipaction.application.favorite;

import kr.co.hanipaction.application.favorite.model.FavoritePostReq;
import kr.co.hanipaction.entity.Favorites;
import kr.co.hanipaction.entity.FavoritesIds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FavoriteRepository extends JpaRepository<Favorites,Long> {
    Favorites findByUserIdAndStoreId(long userId, long storeId);

    @Query("SELECT COUNT(storeId) FROM Favorites WHERE storeId = :storeId")
    Integer findSumFavoritesByStoreId(Long storeId);
}
