package kr.co.hanipaction.application.cart;

import kr.co.hanipaction.application.cart.model.CartListGetRes;
import kr.co.hanipaction.application.cart.model.CartPostReq;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.configuration.model.SignedUser;

import kr.co.hanipaction.configuration.model.UserPrincipal;
import kr.co.hanipaction.entity.Cart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor

public class CartController {
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final CartMenuOptionRepository cartMenuOptionRepository;

//
//
//
//    POST 완료
    @PostMapping
    public ResultResponse<Cart> save(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CartPostReq req) {
        long userId=userPrincipal.getSignedUserId();
        Cart result = cartService.save(req,userId); // 파라미터 추가하고, 유저프린서펄로 넣어주기
        return new ResultResponse<>(200,"메뉴 한개 담기 성공",result);
    }

//
//
//
//    PATCH 인 척 하는 POST 기존 pk 삭제
    @PostMapping("/{cartId}")
    public ResultResponse<Cart> modify(@PathVariable long cartId,@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody CartPostReq req) {
        long userId=userPrincipal.getSignedUserId();
        Cart result = cartService.modify(cartId,req,userId);

        return new ResultResponse<>(200,"메뉴 한개 수정 성공",result);
    }

//
//
//
//    List GET 완료
    @GetMapping
    public ResultResponse<List<CartListGetRes>> findAll(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId= userPrincipal.getSignedUserId();
        List<CartListGetRes> result = cartService.findAll(userId);

        return new ResultResponse<>(200,"카트 리스트 조회 성공",result);
    }

//
//
//
//  1개 GET 완료
    @GetMapping("/{cartId}")
    public ResultResponse<CartListGetRes> findById(@PathVariable long cartId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId= userPrincipal.getSignedUserId();
        CartListGetRes result = cartService.getCartById(userId,cartId);

        return new ResultResponse<>(200,"장바구니 메뉴 1개 조회 성공",result);
    }

//
//
//
//    1개 DELETE 완료
    @DeleteMapping("/{cartId}")
    public ResultResponse<?> deleteOneByCartId(@PathVariable long cartId, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId= userPrincipal.getSignedUserId();
        cartService.delete(cartId,userId);

        return new ResultResponse<>(200,"메뉴가 삭제되었습니다.", null);
    }

//
//
//
//    전체 DELETE 완료
    @DeleteMapping
    public ResultResponse<?> deleteAllByCartId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        long userId= userPrincipal.getSignedUserId();
        cartService.deleteAll(userId);

        return new ResultResponse<>(200,"메뉴 전체가 삭제되었습니다.", null);
    }


}