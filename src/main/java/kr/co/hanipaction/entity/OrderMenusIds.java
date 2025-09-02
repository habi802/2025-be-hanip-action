package kr.co.hanipaction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kr.co.hanipaction.entity.actor.MenuId;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class OrderMenusIds implements Serializable {

    private long orderId;

    private MenuId menuId;
}
