package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;

@Entity
@EqualsAndHashCode
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuOption extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long optionId;

    @ManyToOne
    @JoinColumn(name="menuId", nullable = false)
    private Menu menuId;

    @Column(nullable = false,length = 50)
    private String comment;

    @Column(nullable = false)
    private int price;

}
