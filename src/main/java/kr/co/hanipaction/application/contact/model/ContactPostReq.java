package kr.co.hanipaction.application.contact.model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ContactPostReq {

    private long managerId;
    @NotNull(message = "title은 필수값")
    private String title;
    @NotNull(message = "user_comment는 필수값")
    private String userComment;
    private String managerComment;
    private String imagePath;
}
