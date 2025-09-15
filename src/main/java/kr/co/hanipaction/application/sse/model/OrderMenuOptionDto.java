package kr.co.hanipaction.application.sse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenuOptionDto {
    private Long optionId;
    private String optionName;
    private Long parentId;
    private long optionPrice;
}
