package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.entity.actor.StoreId;
import kr.co.hanipaction.entity.actor.UserId;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

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

    @Column(nullable = false)
    @Comment("유저 아이디")
    private long userId;

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

    //양방향 관계 설정
    @Builder.Default //builder 패턴 이용시 null이 되는데 이 애노테이션을 주면 주소값 생성됨
    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ContactImage> ContactPicList = new ArrayList<>(1);

    public void addContactPics(List<String> picFileNames) {
        for(String picFileName : picFileNames) {
            ContactImageIds contactPicIds = ContactImageIds.builder()
                    .id(this.id)
                    .pic(picFileName)
                    .build();
            ContactImage contactPic = ContactImage.builder()
                    .contactImageIds(contactPicIds)
                    .contact(this)

                    .build();
            this.ContactPicList.add(contactPic);
        }
    }
}
