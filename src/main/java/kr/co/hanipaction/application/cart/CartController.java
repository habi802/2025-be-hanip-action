package kr.co.hanipaction.application.cart;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.hanipaction.application.cart.model.CartDeleteReq;
import kr.co.hanipaction.application.cart.model.CartListGetRes;
import kr.co.hanipaction.application.cart.model.CartPatchReq;
import kr.co.hanipaction.application.cart.model.CartPostReq;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.application.common.util.HttpUtils;
import kr.co.hanipaction.application.user.etc.UserConstants;
import kr.co.hanipaction.configuration.model.SignedUser;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import kr.co.hanipaction.entity.Cart;
import kr.co.hanipaction.entity.CartMenuOption;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor

public class CartController {
    private final CartService cartService;

//JWT 되는지 확인용
    @PostMapping
    public ResultResponse<Cart> save(@AuthenticationPrincipal SignedUser signedUser, @RequestBody CartPostReq req) {
///*        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        long userId=signedUser.signedUserId;

        Cart result = cartService.save(req,userId); // 파라미터 추가하고, 유저프린서펄로 넣어주기
        return new ResultResponse<>("메뉴 한개 담기 성공",result);
    }

    @GetMapping
    public ResultResponse<List<CartListGetRes>> findAll(@AuthenticationPrincipal SignedUser signedUser) {
        long userId=signedUser.signedUserId;


        List<CartListGetRes> result = cartService.findAll(userId);

        return new ResultResponse<>("카트 리스트 조회 성공",result);
    }

//    @PatchMapping
//    public ResponseEntity<ResultResponse<Integer>> updateQuantity(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CartPatchReq req) {
//
//        if (userPrincipal.getSignedUserId() == 0) {
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(ResultResponse.fail(401, "로그인 후 이용해주세요."));
//        }
//
//        int result = cartService.updateQuantity(req, userPrincipal.getSignedUserId());
//        return ResponseEntity.ok(ResultResponse.success(result));
//    }
//
//    @DeleteMapping("/{cartId}")
//    public ResponseEntity<ResultResponse<Integer>> deleteByCartId(@AuthenticationPrincipal UserPrincipal userPrincipal, @PathVariable int cartId) {
//        if (userPrincipal.getSignedUserId() == 0) {
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(ResultResponse.fail(401, "로그인 후 이용해주세요."));
//        }
//
//        CartDeleteReq req = new CartDeleteReq(cartId, userPrincipal.getSignedUserId());
//        int result = cartService.delete(req);
//
//        if (result == 1) {
//            return ResponseEntity.ok(ResultResponse.success(result));
//        }
//        return ResponseEntity.ok(ResultResponse.fail(400, "삭제 실패"));
//    }
//
//    @DeleteMapping
//    public ResponseEntity<ResultResponse<Integer>> deleteByAllUserId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
//        if (userPrincipal.getSignedUserId() == 0) {
//            return ResponseEntity
//                    .status(HttpStatus.UNAUTHORIZED)
//                    .body(ResultResponse.fail(401, "로그인 후 이용해주세요."));
//        }
//
//        int result = cartService.deleteAll(userPrincipal.getSignedUserId());
//        if (result == 1) {
//            return ResponseEntity.ok(ResultResponse.success(result));
//        }
//        return ResponseEntity.ok(ResultResponse.fail(400, "삭제 실패"));
//    }

}