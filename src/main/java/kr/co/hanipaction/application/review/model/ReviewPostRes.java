package kr.co.hanipaction.application.review.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewPostRes {
    private long id;
    private List<String> pics;
}
