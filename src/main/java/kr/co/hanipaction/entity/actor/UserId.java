package kr.co.hanipaction.entity.actor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class UserId {
    @Column(nullable = false)
    private Long userId;
}
