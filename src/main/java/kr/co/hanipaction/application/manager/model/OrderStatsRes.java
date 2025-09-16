package kr.co.hanipaction.application.manager.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class OrderStatsRes {
    private String period;
    private Long total;
    private Long cancelled;
}
