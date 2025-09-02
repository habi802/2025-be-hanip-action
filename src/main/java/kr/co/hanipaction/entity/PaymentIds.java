package kr.co.hanipaction.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
@EqualsAndHashCode
public class PaymentIds implements Serializable {
    private long payId;
    private long orderId;
}
