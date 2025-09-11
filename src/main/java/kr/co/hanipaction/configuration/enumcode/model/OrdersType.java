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
    CARD("01","카드"),
    BANK("02","뱅크"),
    ON_SITE("03","오프라인결제"),
    KAKAO_PAY("04","카카오뱅크"),;

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
