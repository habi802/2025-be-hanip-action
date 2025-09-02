package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import kr.co.hanipaction.entity.actor.MenuId;
import kr.co.hanipaction.entity.actor.MenuOptionId;
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
    // 메뉴 아이디 중복 적용이라 검토해야함
    @EmbeddedId
    private OrderMenusIds orderMenusIds;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id")
    @MapsId("orderId")
    private Orders orderId;

    @Embedded
    private MenuOptionId menuOptionId;
}
