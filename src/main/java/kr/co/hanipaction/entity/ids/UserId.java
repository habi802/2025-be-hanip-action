package kr.co.hanipaction.entity.ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class UserId {
    @Column(nullable = false)
    private Long userId;
}
