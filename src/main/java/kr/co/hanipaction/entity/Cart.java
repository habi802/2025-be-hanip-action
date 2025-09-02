package kr.co.hanipaction.entity;


import jakarta.persistence.*;
import kr.co.hanipaction.entity.actor.MenuId;
import kr.co.hanipaction.entity.actor.StoreId;
import kr.co.hanipaction.entity.actor.UserId;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;
import org.hibernate.annotations.Comment;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
public class Cart extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("카트 아이디")
    private Long id;

    @Embedded
    private UserId userId;

    @Embedded
    private MenuId menuId;

    @Embedded
    private StoreId storeId;

    @Column(nullable = false)
    @Comment("수량")
    private int quantity;



}
