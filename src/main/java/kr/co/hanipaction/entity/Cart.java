package kr.co.hanipaction.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import kr.co.hanipaction.entity.actor.MenuId;
import kr.co.hanipaction.entity.actor.StoreId;
import kr.co.hanipaction.entity.actor.UserId;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
@Setter
public class Cart extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("카트 아이디")
    private Long id;

    @Column
    private long userId;

    @Column
    private long menuId;

    @Column
    private String menuName;

    @Column
    private long storeId;

    @Column
    private int amount;

    @Column(nullable = false)
    @Comment("수량")
    private int quantity;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartMenuOption> options = new ArrayList<>();



}
