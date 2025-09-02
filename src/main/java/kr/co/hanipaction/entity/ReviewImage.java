package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@EqualsAndHashCode
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImage extends UpdatedAt {
    @EmbeddedId
    private ReviewImageIds reviewImageIds;

    @ManyToOne
    @JoinColumn(name="review_id")
    @MapsId("reviewId")
    @Comment("리뷰 아이디")
    private Review review;

    @Column(length = 200)
    @Comment("리뷰 이미지")
    private String imagePath;


}
