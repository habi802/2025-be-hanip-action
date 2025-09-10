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
    private final CartMenuOptionRepository cartMenuOptionRepository;


    //
     // JPA 사용 완료
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
        Map<Long, Long> optionIdToParentIdMap = new HashMap<>();

        for (MenuGetRes menus : menuList) {
            for (MenuGetRes.Option option : menu.getOptions()) {
                collectOptions(option, optionIdToCommentMap, optionIdToPriceMap, optionIdToParentIdMap);
            }
        }


        Cart cart = Cart.builder()
                .menuId(req.getMenuId())
                .menuName(menu.getName())
                .userId(userId)
                .storeId(req.getStoreId())
                .amount(totalAmount)
                .quantity(req.getQuantity())
                .imgPath(menu.getImagePath())
                .build();

        // cart 메뉴에 따라 옵션 테이블 들어감
        List<CartMenuOption> options = req.getOptionId().stream()
                .map(optionId -> CartMenuOption.builder()
                        .menuId(cart)
                        .optionName(optionIdToCommentMap.get(optionId))
                        .optionPrice(optionIdToPriceMap.get(optionId))
                        .optionId(optionId)
                        .parentId(optionIdToParentIdMap.get(optionId))
                        .build())
                .collect(Collectors.toList());


        cart.setOptions(options);

        return cartRepository.save(cart);
    }

    // 옵션의 이름과 가격을 찾는 함수
    private void collectOptions(MenuGetRes.Option option, Map<Long, String> commentMap, Map<Long, Integer> priceMap, Map<Long, Long> childToParentMap) {
        commentMap.put(option.getOptionId(), option.getComment());
        priceMap.put(option.getOptionId(), option.getPrice());
        if (option.getChildren() != null) {
            for (MenuGetRes.Option child : option.getChildren()) {
                childToParentMap.put(child.getOptionId(), option.getOptionId());
                collectOptions(child, commentMap, priceMap, childToParentMap);
            }
        }
    }

    public List<CartListGetRes> findAll(long userId) {
        List<Cart> carts = cartRepository.findAllWithOptions(userId);

        List<Long> optionIds = carts.stream()
                .flatMap(c -> c.getOptions().stream())
                .map(CartMenuOption::getId)
                .toList();

        if (!optionIds.isEmpty()) {
            List<CartMenuOption> optionsWithChildren =
                    cartMenuOptionRepository.findAllWithChildren(optionIds);

            // 엔티티 객체에 children 을 붙여줌 (영속성 컨텍스트에 merge)
            optionsWithChildren.forEach(o -> {
                o.getChildren().size(); // lazy 강제 초기화
            });
        }

        return carts.stream()
                .map(this::toDto)
                .toList();
    }

    public CartListGetRes getCartById(Long userId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "메뉴를 찾을 수 없습니다."));

        return CartListGetRes.fromEntity(cart);
    }

    private CartListGetRes toDto(Cart cart) {
        return CartListGetRes.builder()
                .id(cart.getId())
                .menuId(cart.getMenuId())
                .name(cart.getMenuName())
                .price(cart.getAmount())
                .imagePath(cart.getImgPath())
                .options(cart.getOptions().stream()
                        .filter(o -> o.getParentId() == null)
                        .map(this::toOptionDto)
                        .distinct()
                        .toList())
                .build();
    }
    private CartListGetRes.Option toOptionDto(CartMenuOption option) {
        return CartListGetRes.Option.builder()
                .optionId(option.getOptionId())
                .comment(option.getOptionName())
                .price(option.getOptionPrice())
                .children(option.getChildren().stream()
                        .map(this::toOptionDto)
                        .distinct()
                        .toList())
                .build();
    }

    public int updateQuantity(CartPatchReq req, long userId) {
        CartPatchDto dto = CartPatchDto.builder()
                .cartId(req.getCartId())
                .userId(userId)
                .quantity(req.getQuantity())
                .build();

        return cartMapper.updateQuantityByCartIdAndUserId(dto);
    }
    public int deleteAll(long userId) {
        return cartMapper.deleteByAllUserId(userId);
    }

    public void delete(long cartId, long userId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "메뉴를 찾을 수 없습니다."));

            cartRepository.delete(cart);
    }



}