package kr.co.hanipaction.openfeign.menu.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MenuGetRes {
    private Long menuId;
    private String name;
    private int price;
    private List<Option> options;

    // 메뉴의 옵션
    @Getter
    @Builder
    @ToString
    public static class Option {
        private Long optionId;
        private String comment;
        private int price;
        // 옵션의 하위 옵션
        private List<Option> children;
    }
}
