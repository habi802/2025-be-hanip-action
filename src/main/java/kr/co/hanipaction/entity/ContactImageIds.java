package kr.co.hanipaction.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class ContactImageIds implements Serializable {
    @Comment("이미지 아이디")
    private long id;
    @Comment("문의 이미지")
    private String pic;
}
