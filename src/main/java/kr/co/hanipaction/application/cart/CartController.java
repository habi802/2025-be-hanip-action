package kr.co.hanipaction.application.cart;


import kr.co.hanipaction.application.cart.model.CartDeleteReq;
import kr.co.hanipaction.application.cart.model.CartListGetRes;
import kr.co.hanipaction.application.cart.model.CartPostReq;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.configuration.model.SignedUser;

import kr.co.hanipaction.entity.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

//
//
//
//    POST 완료
    @PostMapping
    public ResultResponse<Cart> save(@AuthenticationPrincipal SignedUser signedUser, @RequestBody CartPostReq req) {
///*        Integer loggedInUserId = (Integer) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        long userId=signedUser.signedUserId;

        Cart result = cartService.save(req,userId); // 파라미터 추가하고, 유저프린서펄로 넣어주기
        return new ResultResponse<>("메뉴 한개 담기 성공",result);
    }

//
//
//
//    List GET 완료
    @GetMapping
    public ResultResponse<List<CartListGetRes>> findAll(@AuthenticationPrincipal SignedUser signedUser) {
        long userId=signedUser.signedUserId;


        List<CartListGetRes> result = cartService.findAll(userId);

        return new ResultResponse<>("카트 리스트 조회 성공",result);
    }

//
//
//
//  1개 GET 완료
    @GetMapping("/{cartId}")
    public ResultResponse<CartListGetRes> findById(@PathVariable long cartId, @AuthenticationPrincipal SignedUser signedUser) {
        long userId=signedUser.signedUserId;

        CartListGetRes result = cartService.getCartById(userId,cartId);

        return new ResultResponse<>("장바구니 메뉴 1개 조회 성공",result);
    }


    @DeleteMapping("/{cartId}")
    public ResultResponse<?> deleteOneByCartId(@PathVariable long cartId, @AuthenticationPrincipal SignedUser signedUser) {
        long userId=signedUser.signedUserId;

        cartService.delete(cartId,userId);

        return new ResultResponse<>("메뉴가 삭제되었습니다.", null);
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
//    public ResponseEntity<ResultResponse<Integer>> deleteByCartId(@AuthenticationPrincipal SignedUser signedUser, @PathVariable int cartId) {
//        long userId=signedUser.signedUserId;
//
//
//        return ResponseEntity.ok(ResultRespon se.fail(400, "삭제 실패"));
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