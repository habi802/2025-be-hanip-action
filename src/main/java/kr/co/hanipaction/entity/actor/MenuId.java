package kr.co.hanipaction.entity.actor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Embeddable
@Getter
public class MenuId {
    @Column(nullable = false)
    @Comment("메뉴 아이디")
    private Long menuId;
}
