package kr.co.hanipaction.application.order.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class OrderGetDto {
    private int userId;
    private int storeId;
}
