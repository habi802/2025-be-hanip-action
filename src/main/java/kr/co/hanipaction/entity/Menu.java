package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.configuration.enumcode.model.MenuType;
import kr.co.hanipaction.entity.actor.StoreId;
import kr.co.hanipaction.entity.actor.UserId;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Menu extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    private StoreId storeId;

    @Embedded
    private UserId userId;

    @Column (nullable = false, length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(nullable = false)
    private int price;

    @Column
    private String imagePath;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT '0'")
    private Integer isSoldOut;

    @Column(nullable = false, length = 2, columnDefinition = "varchar(2) default '01'")
    private MenuType menuType;

    @Builder.Default
    @OneToMany(mappedBy = "menuId", cascade=CascadeType.ALL,orphanRemoval = true)
    private List<MenuOption> menuOptionList = new ArrayList<>(1);

    //기본값 설정
//    @PrePersist
//    public void setDefaultValues() {
//        if (menuType == null) {
//            menuType = MenuType.SINGLE;  // 기본값 설정
//        }
//    }

}
