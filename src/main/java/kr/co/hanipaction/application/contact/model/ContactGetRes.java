package kr.co.hanipaction.application.contact.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactGetRes {
    private long id;
    private long userId;
    private long managerId;
    private String title;
    private String userComment;
    private String managerComment;
    private Date createAt;
}
