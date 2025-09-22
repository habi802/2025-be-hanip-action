package kr.co.hanipaction.configuration.enumcode.model;

import jakarta.persistence.Converter;
import kr.co.hanipaction.configuration.enumcode.AbstractEnumCodeConverter;
import kr.co.hanipaction.configuration.enumcode.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OrdersType implements EnumMapperType {
    NONE("01","미결제"),
    KAKAO_PAY("02","카카오뱅크"),
    NAVER_PAY("03","네이버페이"),
    ;

    private final String code;
    private final String value;

    @Converter(autoApply = true)
    public static class CodeConverter extends AbstractEnumCodeConverter<OrdersType> {
        public CodeConverter() { super(OrdersType.class, false); }
    }

    public static OrdersType valueOfCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown PaymentType code: " + code));
    }
}
