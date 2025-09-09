package kr.co.hanipaction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
public class CartMenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Cart menuId;

    @Column
    private Long optionId;

    @Column
    private String optionName;

    @Column
    private long optionPrice;

    @Column
    private Long parentId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", referencedColumnName = "optionId", insertable = false, updatable = false)
    private List<CartMenuOption> children;

}
