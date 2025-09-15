package kr.co.hanipaction.application.order.newmodel;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderGetDto {
    private String menuName;
    private String storeName;
    private int startIdx;
    private int size;
}
