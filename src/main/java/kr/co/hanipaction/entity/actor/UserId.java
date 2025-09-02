package kr.co.hanipaction.entity.actor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import org.hibernate.annotations.Comment;

@Embeddable
@Getter
public class UserId {
    @Column(nullable = false)
    @Comment("유저 아이디")
    private Long userId;
}
