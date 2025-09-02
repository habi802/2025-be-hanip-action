package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.entity.actor.StoreId;
import kr.co.hanipaction.entity.actor.UserId;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;

@Entity
@EqualsAndHashCode
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private UserId userId;

    @Embedded
    private StoreId storeId;

    @Column
    private long managerId;

    @Column(length = 50)
    private String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String userComment;

    @Column(columnDefinition = "TEXT")
    private String managerComment;
}
