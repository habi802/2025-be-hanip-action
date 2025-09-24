package kr.co.hanipaction.application.cart;

import kr.co.hanipaction.application.cart.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CartMapper {
    int save(CartPostReq req);
    List<CartListGetRes> findAllMenuAndUserId(long userId);
    int updateQuantityByCartIdAndUserId(CartPatchDto dto);
    int deleteByCartId(CartDeleteReq req);
    int deleteByAllUserId(long userId);

    List<CartMenuOptionItem> getByOptions(long userId,long cartId);
}