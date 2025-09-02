package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import kr.co.hanipaction.configuration.enumcode.model.PaymentType;
import kr.co.hanipaction.entity.localDateTime.UpdatedAt;
import lombok.*;

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
    private long payId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id")
    private Orders orderId;

    @Column(nullable = false,length = 20)
    private String tid;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false, length = 2, columnDefinition = "varchar(2) default '01'")
    private PaymentType payment;

}
