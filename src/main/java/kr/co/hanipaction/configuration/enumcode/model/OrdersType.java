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
    KAKAO_PAY("01","카카오뱅크"),;

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
