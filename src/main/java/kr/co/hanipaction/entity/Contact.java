package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.entity.actor.StoreId;
import kr.co.hanipaction.entity.actor.UserId;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;
import org.hibernate.annotations.Comment;

@Entity
@EqualsAndHashCode
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("문의 아이디")
    private long id;

    @Embedded
    private UserId userId;

    @Embedded
    private StoreId storeId;

    @Column
    @Comment("관리자 아이디")
    private long managerId;

    @Column(length = 50, nullable = false)
    @Comment("제목")
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    @Comment("내용")
    private String userComment;

    @Column(columnDefinition = "TEXT")
    @Comment("관리자 답변")
    private String managerComment;

    @Column
    @Comment("문의 이미지")
    private String imagePath;
}
