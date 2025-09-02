package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import kr.co.hanipaction.entity.localDateTime.CreatedAt;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderMenus extends UpdatedAt {
    @EmbeddedId
    private OrderMenusIds orderMenusIds;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id")
    @MapsId("orderId")
    private Orders orderId;

    // 포링키 안 걸림, 이유 찾아봐야함
    @ManyToOne
    @JoinColumn(name="menu_id")
    @MapsId("menuId")
    private Menu menuId;

    @ManyToOne
    @JoinColumn(name="option_id")
    private MenuOption menuOption;

}
