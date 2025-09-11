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
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class OrdersMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private Long menuId;
    @Column(nullable = false)
    private long storeId;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false,length = 250)
    private String menuName;

    @Column(nullable = false)
    private long quantity;

    @Column(nullable = false,length = 250)
    private String menuImg;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Orders orders;

    @OneToMany(mappedBy = "ordersItem", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrdersMenuOption> options = new ArrayList<>();
}
