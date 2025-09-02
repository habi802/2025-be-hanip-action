package kr.co.hanipaction.entity.ids;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
public class StoreId {
    @Column(nullable = false)
    private Long storeId;
}
