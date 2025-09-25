package kr.co.hanipaction.application.cart;

import jakarta.transaction.Transactional;
import kr.co.hanipaction.application.cart.model.*;
import kr.co.hanipaction.entity.Cart;
import kr.co.hanipaction.entity.CartMenuOption;
import kr.co.hanipaction.openfeign.menu.MenuClient;
import kr.co.hanipaction.openfeign.menu.model.MenuGetReq;
import kr.co.hanipaction.openfeign.menu.model.MenuGetRes;
import kr.co.hanipaction.openfeign.store.StoreClient;
import kr.co.hanipaction.openfeign.store.model.StoreGetRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import kr.co.hanipaction.application.common.model.ResultResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {
    private final MenuClient  menuClient;
    private final StoreClient storeClient;
    private final CartRepository cartRepository;
    private final CartMenuOptionRepository cartMenuOptionRepository;
    private final CartMapper cartMapper;

    public Cart save(CartPostReq req, long userId) {

        MenuGetReq menuGetReq = new MenuGetReq();
        menuGetReq.setMenuIds(Collections.singletonList((req.getMenuId())));
        menuGetReq.setOptionIds(req.getOptionId());

        ResultResponse<List<MenuGetRes>> menuRes = menuClient.getOrderMenu(menuGetReq);


        if (menuRes == null || menuRes.getResultData() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "메뉴 정보를 호출 할 수 없습니다.");
        }
        MenuGetRes menu = menuRes.getResultData().get(0);


        long storeId = menu.getStoreId();

        ResultResponse<StoreGetRes> storeRes = storeClient.findStore(storeId);

        StoreGetRes store =  storeRes.getResultData();


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
        int totalAmount =  (menuPrice + totalOptionPrice);

        List<MenuGetRes> menuList = menuRes.getResultData();

        Map<Long, String> optionIdToCommentMap = new HashMap<>();
        Map<Long, Integer> optionIdToPriceMap = new HashMap<>();
        Map<Long, Long> optionIdToParentIdMap = new HashMap<>();

        Cart cart = Cart.builder()
                .menuId(req.getMenuId())
                .menuName(menu.getName())
                .price(menu.getPrice())
                .userId(userId)
                .amount(totalAmount)
                .quantity(req.getQuantity())
                .imgPath(menu.getImagePath())
                .storeId(storeId)
                .storeName(store.getName())
                .build();

        for (MenuGetRes menus : menuList) {
            for (MenuGetRes.Option option : menu.getOptions()) {
                collectOptions(option, optionIdToCommentMap, optionIdToPriceMap, optionIdToParentIdMap);
            }
        }


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

    //말이 수정이지 삭제했다가 다시 올리는 겁니다.
    public Cart modify(long cartId,CartPostReq req, long userId) {

        Cart cartPk = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "메뉴를 찾을 수 없습니다."));

        cartRepository.delete(cartPk);


        return save(req, userId);


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

// 카트에 별도 배달료 컬럼 넣기 애매해서, 장바구니 페이지에 보여지는 배달료와 합계는 프론트에서 적용해주는 걸로,
//    프론트에서 숫자 바꿔서 날려보내도  실제 거래 가격은 오더쪽에서 배달료 + 카트 메뉴 합계로 RES 처리해서 상관 없음
    public List<CartListGetRes> findAll(long userId) {
        List<Cart> carts = cartRepository.findAllWithOptions(userId);

        List<Long> optionIds = carts.stream()
                .flatMap(c -> c.getOptions().stream())
                .map(CartMenuOption::getId)
                .toList();

        if (!optionIds.isEmpty()) {
            List<CartMenuOption> optionsWithChildren =
                    cartMenuOptionRepository.findAllWithChildren(optionIds);

            optionsWithChildren.forEach(o -> {
                o.getChildren().size();
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
                .storeId(cart.getStoreId())
                .storeName(cart.getStoreName())
                .quantity(cart.getQuantity())
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

    public void delete(long cartId, long userId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "메뉴를 찾을 수 없습니다."));

            cartRepository.delete(cart);
    }


    public void deleteAll(long userId) {
    cartRepository.deleteAll();
    }


    @Transactional
    public Cart plusQuantity(long userId,long cartId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);


            Cart cart = cartOptional.get();
        int menuAmount = cart.getAmount() / cart.getQuantity();
        int modifyQuantity = cart.getQuantity() + 1;

            cart.setQuantity(modifyQuantity);
            cart.setAmount(menuAmount * modifyQuantity );

        return cart;
    }

    @Transactional
    public Cart minusQuantity(long userId,long cartId) {
        Optional<Cart> cartOptional = cartRepository.findById(cartId);

        Cart cart = cartOptional.get();

        int menuAmount = cart.getAmount() / cart.getQuantity();
        int modifyQuantity = cart.getQuantity() -1;

        cart.setQuantity(modifyQuantity);
        cart.setAmount(menuAmount * modifyQuantity );

        return cart;
    }

    @Transactional
    public CartGetOptionRes check(Long cartId, Long userId) {
        List<CartMenuOptionItem> items = cartMapper.getByOptions(userId, cartId);

        CartGetOptionRes dto = new CartGetOptionRes();
        if (!items.isEmpty()) {
            dto.setMenuId(items.get(0).getMenuId());
            dto.setOptionId(items.stream()
                    .map(CartMenuOptionItem::getOptionId)
                    .collect(Collectors.toList()));
            dto.setQuantity(items.get(0).getQuantity());
        }
        return dto;
    }


}