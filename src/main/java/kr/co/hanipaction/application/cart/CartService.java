package kr.co.hanipaction.application.cart;

import kr.co.hanipaction.application.cart.model.*;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.entity.Cart;
import kr.co.hanipaction.entity.CartMenuOption;
import kr.co.hanipaction.openfeign.menu.MenuClient;
import kr.co.hanipaction.openfeign.menu.model.MenuGetItem;
import kr.co.hanipaction.openfeign.menu.model.MenuGetOption;
import kr.co.hanipaction.openfeign.menu.model.MenuGetReq;
import kr.co.hanipaction.openfeign.menu.model.MenuGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final CartMapper cartMapper;
    private final MenuClient  menuClient;
    private final CartRepository cartRepository;

    public Cart save(CartPostReq req, long userId) {

        MenuGetReq menuGetReq = new MenuGetReq();
        menuGetReq.setMenuIds(Collections.singletonList((req.getMenuId())));
        menuGetReq.setOptionIds(req.getOptionId());

        ResultResponse<List<MenuGetRes>>  menuRes = menuClient.getOrderMenu(menuGetReq);


        if (menuRes == null || menuRes.getResult() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "메뉴 정보를 호출 할 수 없습니다.");
        }
        MenuGetRes menu = menuRes.getResult().get(0);

        int menuPrice = menu.getPrice();


        int totalOptionPrice = 0;
        List<Long> selectedOptionIds = req.getOptionId();


        for(MenuGetRes.Option option : menu.getOptions()){

            if(selectedOptionIds.contains(option.getOptionId())) {
                totalOptionPrice += option.getPrice();
            }
            if(option.getChildren() != null){
                for(MenuGetRes.Option child : option.getChildren()){
                    if(selectedOptionIds.contains(child.getOptionId())) {
                        totalOptionPrice += child.getPrice();
                    }
                }
            }

        }
        int totalAmount =  (menuPrice + totalOptionPrice) * req.getQuantity();

        List<MenuGetRes> menuList = menuRes.getResult();

        Map<Long, String> optionIdToCommentMap = new HashMap<>();
        Map<Long, Integer> optionIdToPriceMap = new HashMap<>();
        for (MenuGetRes menus : menuList) {
            for (MenuGetRes.Option option : menu.getOptions()) {
                collectOptions(option, optionIdToCommentMap, optionIdToPriceMap);
            }
        }


        Cart cart = Cart.builder()
                .menuId(req.getMenuId())
                .menuName(menu.getName())
                .userId(userId)
                .storeId(req.getStoreId())
                .amount(totalAmount)
                .quantity(req.getQuantity())
                .build();

        // cart 메뉴에 따라 옵션 테이블 들어감
        List<CartMenuOption> options = req.getOptionId().stream()
                .map(optionId -> CartMenuOption.builder()
                        .menuId(cart)
                        .optionName(optionIdToCommentMap.get(optionId))
                        .optionPrice(optionIdToPriceMap.get(optionId))
                        .optionId(optionId)
                        .build())
                .collect(Collectors.toList());


        cart.setOptions(options);

        return cartRepository.save(cart);
    }

    // 옵션의 이름과 가격을 찾는 함수
    private void collectOptions(MenuGetRes.Option option, Map<Long, String> commentMap, Map<Long, Integer> priceMap) {
        commentMap.put(option.getOptionId(), option.getComment());
        priceMap.put(option.getOptionId(), option.getPrice());
        if (option.getChildren() != null) {
            for (MenuGetRes.Option child : option.getChildren()) {
                collectOptions(child, commentMap, priceMap);
            }
        }
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


//    public List<MenuGetRes> callMenuClient(List<Long> menuIds, List<Long> optionIds) {
//        MenuGetReq req = new MenuGetReq();
//        req.setMenuIds(menuIds);
//        req.setOptionIds(optionIds);
//
//        ResultResponse<List<MenuGetRes>> response = menuClient.getOrderMenu(req);
//
//        if (response == null || response.getResult() == null) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "메뉴 정보를 불러올 수 없습니다.");
//        }
//
//        return response.getResult();  // List<MenuGetRes> 반환
//    }

}