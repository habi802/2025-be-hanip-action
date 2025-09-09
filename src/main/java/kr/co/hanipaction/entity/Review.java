package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@EqualsAndHashCode
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("리뷰 아이디")
    private long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @Comment("오더 아이디")
    private Orders orderId;

    @Column
    @Comment("유저 아이디")
    private long userId;

    @Column
    @Comment("별점")
    private double rating;

    @Column(columnDefinition = "TEXT")
    @Comment("내용")
    private String comment;

    @Column(columnDefinition = "TEXT")
    @Comment("오너 코멘트")
    private String ownerComment;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT '0'")
    @Comment("리뷰 숨김 처리, (0: 비활성화, 1: 활성화)")
    private Integer isHide;

    @PrePersist
    public void prePersist() {
        if (this.isHide == null) {
            this.isHide = 0;
        }
    }
    //양방향 관계 설정
    @Builder.Default //builder 패턴 이용시 null이 되는데 이 애노테이션을 주면 주소값 생성됨
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> ReviewPicList = new ArrayList<>(1);

    public void addReviewPics(List<String> picFileNames) {
        for (String picFileName : picFileNames) {
            ReviewImageIds reviewPicIds = ReviewImageIds.builder()
                    .id(this.id)
                    .pic(picFileName)
                    .build();
            ReviewImage reviewPic = ReviewImage.builder()
                    .reviewImageIds(reviewPicIds)
                    .review(this)

                    .build();
            this.ReviewPicList.add(reviewPic);
        }
    }

}
