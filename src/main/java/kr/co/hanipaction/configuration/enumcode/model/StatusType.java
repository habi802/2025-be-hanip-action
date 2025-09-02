package kr.co.hanipaction.configuration.enumcode.model;

import jakarta.persistence.Converter;
import kr.co.hanipaction.configuration.enumcode.AbstractEnumCodeConverter;
import kr.co.hanipaction.configuration.enumcode.EnumMapperType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusType implements EnumMapperType {
    ORDERED("01", "준비중"),
    PREPARING("02", "음식준비중"),
    DELIVERED("03", "배달중"),
    COMPLETED("04", "배달완료"),
    CANCELED("05", "주문취소"),;

    private final String code;
    private final String value;

    @Converter(autoApply = true)
    public static class CodeConverter extends AbstractEnumCodeConverter<StatusType> {
        public CodeConverter() { super(StatusType.class, false); }
    }

}
