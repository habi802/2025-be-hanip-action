package kr.co.hanipaction.application.cart.model;

import kr.co.hanipaction.entity.Cart;
import kr.co.hanipaction.entity.CartMenuOption;
import lombok.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CartListGetRes {
    private long id;
    private Long menuId;
    private String name;
    private long price;
    private String comment;
    private String imagePath;
    private List<Option> options;

    @Getter
    @Builder
    @ToString
    public static class Option {
        private Long optionId;
        private String comment;
        private long price;
        // 옵션의 하위 옵션
        private List<CartListGetRes.Option> children;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Option)) return false;
            Option that = (Option) o;
            return Objects.equals(optionId, that.optionId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(optionId);
        }


    }

    // 개인 조회를 위한 Dto 제작 용
    public static CartListGetRes fromEntity(Cart cart) {
        CartListGetRes response = new CartListGetRes();
        // 해당 Pk의 에 대한 정보들 조회
        response.setId(cart.getId());
        response.setMenuId(cart.getMenuId());
        response.setName(cart.getMenuName());
        response.setPrice(cart.getAmount());
        response.setImagePath(cart.getImgPath());

        if (cart.getOptions() != null) {
            // 옵션을 선택한다면..  부모만 처음 필터
            List<CartListGetRes.Option> optionList = cart.getOptions().stream()
                    .filter(opt -> opt.getParentId() == null)
                    .map(opt -> toOptionDto(opt, cart.getOptions())) // 하위에 옵션에 대한 List도 추가
                    .collect(Collectors.toList());
            response.setOptions(optionList);
        }

        return response;

    }

    private static CartListGetRes.Option toOptionDto(CartMenuOption option, List<CartMenuOption> allOptions) {
    // 하위옵션들을 표시하기 위한 Dto 메소드
        List<CartListGetRes.Option> children = allOptions.stream()
                .filter(child -> option.getOptionId().equals(child.getParentId())) // 자식 옵션을 추가
                .map(child -> toOptionDto(child, allOptions))
                .collect(Collectors.toList());

        return CartListGetRes.Option.builder()
                .optionId(option.getOptionId())
                .comment(option.getOptionName())
                .price(option.getOptionPrice())
                .children(children)
                .build();
    }
}
