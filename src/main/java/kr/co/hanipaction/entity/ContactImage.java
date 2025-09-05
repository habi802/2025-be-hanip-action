package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.entity.localDateTime.CreatedAt;
import lombok.*;
import org.hibernate.annotations.Comment;


@Entity
@EqualsAndHashCode
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactImage extends CreatedAt {

    @EmbeddedId
    private ContactImageIds contactImageIds;

    @ManyToOne
    @JoinColumn(name="contact_id")
    @MapsId("id")
    private Contact contact;

}
