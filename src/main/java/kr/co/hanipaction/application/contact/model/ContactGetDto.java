package kr.co.hanipaction.application.contact.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactGetDto {
    private long startIdx;
    private long size;
    private long userId;
}
