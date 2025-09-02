package kr.co.hanipaction.application.menu.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MenuGetReq {
    private int menuId;
    private int storeId;
    private String name;
    private String comment;
    private int price;


}
