package kr.co.hanipaction.application.menu.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MenuPutReq {
    private long id;
    private long userId;
    private String name;
    private String comment;
    private int price;
    private String imagePath;

}
