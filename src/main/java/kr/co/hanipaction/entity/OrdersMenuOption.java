package kr.co.hanipaction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrdersMenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long optionId; // 메뉴 서비스의 옵션 PK

    @Column(nullable = false,length = 100)
    private String optionName;

    @Column(nullable = false)
    private long optionPrice;

    @Column
    private Long parentId;

    @Column(nullable = false)
    private long menuId;

    @ManyToOne
    @JoinColumn(name = "orders_item_id")
    @JsonBackReference
    private OrdersMenu ordersItem;

    @OneToMany(mappedBy = "parentId", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrdersMenuOption> children = new ArrayList<>();
}
