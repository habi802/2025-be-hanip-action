package kr.co.hanipaction.entity;


import jakarta.persistence.*;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
public class Cart extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private UserId userId;

    @ManyToOne
    @JoinColumn(nullable = false, name = "menu_id")
    private Menu menuId;

    @Embedded
    private StoreId storeId;

    @Column(nullable = false)
    private int quantity;



}
