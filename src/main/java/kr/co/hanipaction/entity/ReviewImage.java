package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;

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
    private Review review;

    @Column(length = 200)
    private String imagePath;


}
