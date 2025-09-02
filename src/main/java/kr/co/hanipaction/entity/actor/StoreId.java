package kr.co.hanipaction.entity.actor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Comment;

@Embeddable
@Getter
public class StoreId {
    @Column(nullable = false)
    @Comment("가게 아이디")
    private Long storeId;
}
