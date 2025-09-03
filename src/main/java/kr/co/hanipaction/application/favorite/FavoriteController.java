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
        if (signedUser.signedUserId == 0) {
            return ResultResponse.success(null);
        }

        req.setUserId(signedUser.signedUserId);
        int result = favoriteService.save(req);
        return ResultResponse.success(result);
    }

    @GetMapping
    public ResultResponse<List<FavoriteGetRes>> findAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
//        int userId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        return ResultResponse.success(favoriteService.findAll(userPrincipal.getSignedUserId()));
    }

    @GetMapping("/{store_id}")
    public ResultResponse<Integer> find(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable("store_id") int storeId) {
//        Integer userId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        if (userPrincipal.getSignedUserId() == 0) {
            return ResultResponse.success(null);
        }

        return ResultResponse.success(favoriteService.find(storeId, userPrincipal.getSignedUserId()));
    }

    @DeleteMapping("/{store_id}")
    public ResultResponse<Integer> delete(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable("store_id") int storeId) {
//        int userId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        return ResultResponse.success(favoriteService.delete(userPrincipal.getSignedUserId(), storeId));
    }
}


