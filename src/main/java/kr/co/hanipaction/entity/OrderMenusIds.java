package kr.co.hanipaction.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kr.co.hanipaction.entity.actor.MenuId;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class OrderMenusIds implements Serializable {
    @Comment("오더 아이디")
    private long orderId;

    private MenuId menuId;
}
