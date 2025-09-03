package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.configuration.enumcode.model.OrdersType;
import kr.co.hanipaction.configuration.enumcode.model.StatusType;
import kr.co.hanipaction.entity.actor.StoreId;
import kr.co.hanipaction.entity.actor.UserId;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

@Entity
@EqualsAndHashCode
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("오더 아이디")
    private long id;

    @Column(nullable = false)
    private UserId userId;

    @Column(nullable = false)
    private StoreId storeId;

    @Column(nullable = false, length = 12)
    @Comment("우편 번호")
    private String postcode;

    @Column(nullable = false, length = 100)
    @Comment("배달 주소")
    private String address;

    @Column(length = 100)
    @Comment("상세 주소")
    private String addressDetail;

    @Column
    @Comment("결제 금액")
    private int amount;

    @Column(columnDefinition = "TEXT")
    @Comment("요청 사항 (가게)")
    private String storeRequest;

    @Column(columnDefinition = "TEXT")
    @Comment("요청 사항 (라이더)")
    private String riderRequest;

    @Column(nullable = false, length = 2)
    @Comment("결제 방식")
    private OrdersType payment;

    @Column(nullable = false, length = 2, columnDefinition = "varchar(2) default '01'")
    @Comment("주문 상태")
    private StatusType status;

    @Column(nullable = false, columnDefinition = "TINYINT(1) DEFAULT '0'")
    @Comment("삭제 여부")
    private Integer isDeleted;

}
