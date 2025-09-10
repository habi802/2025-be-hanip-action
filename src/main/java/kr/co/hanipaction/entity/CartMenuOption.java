package kr.co.hanipaction.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
@Getter
public class CartMenuOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    @JsonBackReference
    private Cart menuId;

    @Column
    @JsonBackReference
    private Long optionId;

    @Column
    private String optionName;

    @Column
    private long optionPrice;

    @Column
    private Long parentId;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentId", referencedColumnName = "optionId", insertable = false, updatable = false)
    @JsonManagedReference
    private List<CartMenuOption> children = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CartMenuOption)) return false;
        CartMenuOption that = (CartMenuOption) o;
        return Objects.equals(optionId, that.optionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(optionId);
    }

}
