package kr.co.hanipaction.application.favorite;

import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.favorite.model.FavoriteGetRes;
import kr.co.hanipaction.application.favorite.model.FavoritePostReq;
import kr.co.hanipaction.configuration.model.SignedUser;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;

    // 찜 등록
    @PostMapping
    public ResultResponse<Integer> save(@RequestBody FavoritePostReq req, @AuthenticationPrincipal SignedUser signedUser) {
        long userId = signedUser.signedUserId;
        log.info("signed userId: {}", userId);

        if (userId == 0) {
            return ResultResponse.success(null);
        }

        int result = favoriteService.save(userId,req);
        return ResultResponse.success(result);
    }

    // 자신의 찜 목록 조회
    @GetMapping
    public ResultResponse<List<FavoriteGetRes>> findAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResultResponse.success(favoriteService.findAll(userPrincipal.getSignedUserId()));
    }

    // ?
    @GetMapping("/{store_id}")
    public ResultResponse<Integer> find(@PathVariable("store_id") Long storeId) {
        return ResultResponse.success(favoriteService.find(storeId));
    }

    // 찜 삭제
    @DeleteMapping("/{store_id}")
    public ResultResponse<Integer> delete(@AuthenticationPrincipal SignedUser signedUser, @PathVariable("store_id") Long storeId) {
        favoriteService.delete(signedUser.signedUserId, storeId);

        return ResultResponse.success(1);
    }

    // 유저 좋아요 유무 (서버 API)
    @GetMapping("/count")
    public boolean getStoreFavorites(@RequestParam("store_id") Long storeId,
                                                              @RequestParam(value = "user_id", required = false) Long userId) {
        boolean result = favoriteService.getStoreFavorites(storeId, userId);
        return result;
    }
}


