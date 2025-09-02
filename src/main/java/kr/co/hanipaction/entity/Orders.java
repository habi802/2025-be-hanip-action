package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.configuration.enumcode.model.OrdersType;
import kr.co.hanipaction.configuration.enumcode.model.StatusType;
import kr.co.hanipaction.entity.actor.StoreId;
import kr.co.hanipaction.entity.actor.UserId;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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

    @Column(nullable = false, length = 2)
    private OrdersType payment;

    @Column(nullable = false, length = 2)
    private StatusType status;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT '0'")
    private Integer isDeleted;

}
