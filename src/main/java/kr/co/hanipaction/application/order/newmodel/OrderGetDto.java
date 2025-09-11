package kr.co.hanipaction.application.order.newmodel;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderGetDto {
    private String menuName;
    private String StoreName;
    private int startIdx;
    private int size;
}
