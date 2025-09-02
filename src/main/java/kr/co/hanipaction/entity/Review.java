package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@EqualsAndHashCode
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("리뷰 아이디")
    private long id;

    @ManyToOne
    @JoinColumn(name="order_id")
    private Orders orderId;

    @Column(nullable = false, precision = 2, scale = 1)
    @Comment("별점")
    private BigDecimal rating;

    @Column(columnDefinition = "TEXT")
    @Comment("내용")
    private String comment;

    @Column(columnDefinition = "TEXT")
    @Comment("오너 코멘트")
    private String ownerComment;
}
