package kr.co.hanipaction.configuration.enumcode.model;

import jakarta.persistence.Converter;
import kr.co.hanipaction.configuration.enumcode.AbstractEnumCodeConverter;
import kr.co.hanipaction.configuration.enumcode.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum StatusType implements EnumMapperType {
    ORDERED("01", "결제 확인"),
    PAID("02", "결제 완료"),
    PREPARING("03", "음식준비중"),
    DELIVERED("04", "배달중"),
    COMPLETED("05", "배달완료"),
    CANCELED("06", "주문취소"),
    ;

    private final String code;
    private final String value;

    @Converter(autoApply = true)
    public static class CodeConverter extends AbstractEnumCodeConverter<StatusType> {
        public CodeConverter() { super(StatusType.class, false); }
    }

    public static StatusType valueOfCode(String code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown PaymentType code: " + code));
    }
}
