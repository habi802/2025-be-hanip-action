package kr.co.hanipaction.application.favorite;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.common.util.HttpUtils;
import kr.co.hanipaction.application.favorite.model.FavoriteGetRes;
import kr.co.hanipaction.application.favorite.model.FavoritePostReq;
import kr.co.hanipaction.application.user.etc.UserConstants;
import kr.co.hanipaction.configuration.model.SignedUser;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import kr.co.hanipaction.entity.actor.UserId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/favorite")
@RequiredArgsConstructor
public class FavoriteController {
    private final FavoriteService favoriteService;



    @PostMapping
    public ResultResponse<Integer> save(@RequestBody FavoritePostReq req, @AuthenticationPrincipal SignedUser signedUser) {
//        Integer userId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
//        long userId = signedUser.getSignedUserId();

        long userId = signedUser.signedUserId;
        log.info("signed userId: {}", userId);
//        log.info("signed user : {}",signedUser);
        if (userId == 0) {
            return ResultResponse.success(null);
        }
        int result = favoriteService.save(userId,req);
        return ResultResponse.success(result);



    }

    @GetMapping
    public ResultResponse<List<FavoriteGetRes>> findAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
//        int userId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        return ResultResponse.success(favoriteService.findAll(userPrincipal.getSignedUserId()));
    }

    @GetMapping("/{store_id}")
    public ResultResponse<Integer> find(@PathVariable("store_id") int storeId) {

        return ResultResponse.success(favoriteService.find(storeId));
    }

    @DeleteMapping("/{store_id}")
    public ResultResponse<Integer> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable("store_id") int storeId) {
//        int userId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        return ResultResponse.success(favoriteService.delete(userPrincipal.getSignedUserId(), storeId));
    }
}


