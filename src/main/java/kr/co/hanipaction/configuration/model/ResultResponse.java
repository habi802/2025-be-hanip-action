package kr.co.hanipaction.configuration.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ResultResponse<T> {
    private String message;
    @JsonProperty("resultData")
    private T result;
}
