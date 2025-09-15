package kr.co.hanipaction.application.favorite;

import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.favorite.model.FavoriteGetRes;
import kr.co.hanipaction.application.favorite.model.FavoritePostReq;
import kr.co.hanipaction.configuration.model.SignedUser;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;
//
//
//
// 찜 등록
    @PostMapping
    public ResponseEntity<ResultResponse<Integer>> save(@RequestBody FavoritePostReq req, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId = userPrincipal.getSignedUserId();
        log.info("signed userId: {}", userId);

        int result = favoriteService.save(userId,req);
        return ResponseEntity.ok(new ResultResponse<>(200, "해당 가게를 찜했습니다", result));
    }
//
//
//
// 자신의 찜 목록 조회
    @GetMapping
    public ResultResponse<List<FavoriteGetRes>> findAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId = userPrincipal.getSignedUserId();

        return ResultResponse.success(favoriteService.findAll(userId));
    }

//
//
//
// 내가 해당 가게를 찜했는지 확인
    @GetMapping("/{store_id}")
    public ResponseEntity<ResultResponse<Integer>> find(@PathVariable("store_id") Long storeId) {
        ResultResponse.success(favoriteService.find(storeId));
        
        return ResponseEntity.ok(new ResultResponse<>(200, "해당 가게를 찜하고 있습니다", 1));
    }

//
//
//
// 찜 삭제
    @DeleteMapping("/{store_id}")
    public ResponseEntity<ResultResponse<Integer>> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable("store_id") Long storeId) {
         favoriteService.delete(userPrincipal.getSignedUserId(), storeId);
         
        return ResponseEntity.ok(new ResultResponse<>(200, "해당 가게 찜삭제 성공", 1));
    }



    // 유저 좋아요 유무 (서버 API)
    @GetMapping("/count")
    public boolean getStoreFavorites(@RequestParam("store_id") Long storeId,
                                                              @RequestParam(value = "user_id", required = false) Long userId) {
        boolean result = favoriteService.getStoreFavorites(storeId, userId);
        return result;
    }
}


