package kr.co.hanipaction.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class ReviewImageIds implements Serializable {
    @Comment("이미지 아이디")
    private long id;
    @Comment("리뷰 아이디")
    private long reviewId;
}
