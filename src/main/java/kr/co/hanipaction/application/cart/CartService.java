package kr.co.hanipaction.application.cart;

import kr.co.hanipaction.application.cart.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;

    public int save(CartPostReq req, long userId) {
        int result = cartMapper.save(req);
        return req.getCartId();
    }

    public List<CartListGetRes> findAll(long userId) {
        return cartMapper.findAllMenuAndUserId(userId);
    }

    public int updateQuantity(CartPatchReq req, long userId) {
        CartPatchDto dto = CartPatchDto.builder()
                .cartId(req.getCartId())
                .userId(userId)
                .quantity(req.getQuantity())
                .build();

        return cartMapper.updateQuantityByCartIdAndUserId(dto);
    }

    public int delete(CartDeleteReq req) {
        return cartMapper.deleteByCartId(req);
    }

    public int deleteAll(long userId) {
        return cartMapper.deleteByAllUserId(userId);
    }

}