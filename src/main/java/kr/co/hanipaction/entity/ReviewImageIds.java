package kr.co.hanipaction.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class ReviewImageIds implements Serializable {
    private long id;
    private long reviewId;
}
