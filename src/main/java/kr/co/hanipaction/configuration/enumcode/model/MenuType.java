package kr.co.hanipaction.configuration.enumcode.model;

import jakarta.persistence.Converter;
import kr.co.hanipaction.configuration.enumcode.AbstractEnumCodeConverter;
import kr.co.hanipaction.configuration.enumcode.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum MenuType implements EnumMapperType {
    SINGLE("01", "단품 메뉴")
    , SET("02", "세트 메뉴")
    , SIDE("03", "사이드 메뉴")
    , DRINK("04", "음료")
    ;


    private final String code;
    private final String value;

    @Converter(autoApply = true)
    public static class CodeConverter extends AbstractEnumCodeConverter<MenuType> {
        public CodeConverter() { super(MenuType.class, false); }
    }
}
