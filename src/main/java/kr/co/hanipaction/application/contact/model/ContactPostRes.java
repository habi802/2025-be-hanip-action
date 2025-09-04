package kr.co.hanipaction.application.contact.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
public class ContactPostRes {
    private long id;
    private List<String> pics;
}
