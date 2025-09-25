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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
@Setter
@Getter
public class Cart extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("카트 아이디")
    private Long id;

    @Column (nullable = false)
    private long userId;

    @Column (nullable = false)
    private long menuId;

    @Column (nullable = false)
    private String storeName;

    @Column (nullable = false,length = 250)
    private String menuName;

    @Column (nullable = false)
    private long storeId;

    @Column (nullable = false)
    private int amount;

    @Column(nullable = false)
    @Comment("수량")
    private int quantity;

    @Column (nullable = false,length = 250)
    @Comment("이미지 경로")
    private String imgPath;

    @Column (nullable = false)
    @Comment("메뉴 가격")
    private int price;

    @OneToMany(mappedBy = "menuId", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CartMenuOption> options = new ArrayList<>();



}
