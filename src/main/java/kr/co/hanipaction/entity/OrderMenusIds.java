package kr.co.hanipaction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class OrderMenusIds implements Serializable {
    @Column(name = "order_id")
    private long orderId;
    @Column(name = "menu_id")
    private long menuId;
}
