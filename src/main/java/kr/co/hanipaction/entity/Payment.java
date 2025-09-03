package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.configuration.enumcode.model.PaymentType;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;
import org.hibernate.annotations.Comment;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
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

    @Column(nullable = false,length = 20)
    @Comment("tid")
    private String tid;

    @Column(nullable = false)
    @Comment("결제 금액")
    private int amount;

    @Column(nullable = false, length = 2, columnDefinition = "varchar(2) default '01'")
    @Comment("결제 상태")
    private PaymentType status;

}
