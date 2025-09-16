package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.configuration.enumcode.model.PaymentType;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

import static kr.co.hanipaction.configuration.enumcode.model.PaymentType.UNPAID;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Payment extends UpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("결제 아이디")
    private long payId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id", foreignKey = @ForeignKey(name = "fk_order_id"))
    @Comment("오더 아이디")
    private Orders orderId;

    @Column(nullable = false)
    @Comment("메뉴 이름")
    private String itemName;

    @Column
    @Comment("총 주문금액")
    private int totalAmount;
    @Column
    @Comment("비과세")
    private int taxFreeAmount;

    @Column(length = 20)
    @Comment("tid")
    private String tid;

    @Column(nullable = false)
    @Comment("메뉴 갯수")
    private int quantity;

    @Column(nullable = false, length = 2, columnDefinition = "varchar(2) default '01'")
    @Comment("결제 상태")
    private PaymentType status;

    @PrePersist
    public void prePersist() {

        if (this.status == null) {
            this.status = UNPAID;
        }
    }

}
