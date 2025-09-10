package kr.co.hanipaction.application.cart.model;

import kr.co.hanipaction.entity.CartMenuOption;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@ToString
@Builder
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


}
