package kr.co.hanipaction.openfeign.menu.model;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuGetItem {
    private Long menuId;
    private String name;
    private int price;
    private List<MenuGetOption> options;

}
