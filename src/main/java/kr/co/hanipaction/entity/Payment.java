package kr.co.hanipaction.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Payment {
    @EmbeddedId
    private PaymentIds paymentIds;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="order_id")
    @MapsId("orderId")
    private Orders orderId;

    @Column(nullable = false,length = 20)
    private String tid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="amount")
    private Orders amount;


}
