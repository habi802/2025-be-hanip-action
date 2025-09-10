package kr.co.hanipaction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrdersMenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long optionId; // 메뉴 서비스의 옵션 PK

    @ManyToOne
    @JoinColumn(name = "orders_item_id")
    @JsonBackReference
    private OrdersMenu ordersItem;
}
