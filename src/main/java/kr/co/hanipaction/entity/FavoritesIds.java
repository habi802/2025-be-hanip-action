package kr.co.hanipaction.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import kr.co.hanipaction.entity.actor.StoreId;
import kr.co.hanipaction.entity.actor.UserId;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class FavoritesIds implements Serializable {

    private long storeId;

    private long userId;
}
