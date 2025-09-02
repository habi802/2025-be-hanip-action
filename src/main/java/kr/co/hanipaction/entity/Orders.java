package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.entity.enums.PaymentType;
import kr.co.hanipaction.entity.enums.StatusType;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Embedded
    private UserId userId;

    @Embedded
    private StoreId storeId;

    @Column(nullable = false, length = 12)
    private String postcode;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(length = 100)
    private String addressDetail;

    @Column
    private int amount;

    @Column(columnDefinition = "TEXT")
    private String storeRequest;

    @Column(columnDefinition = "TEXT")
    private String riderRequest;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType statusType;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private int isDeleted;

}
