package kr.co.hanipaction.openfeign.menu.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class MenuGetOption {
    private Long  id;
    private String name;
    private int price;
    private List<MenuGetOption> subOptions = new ArrayList<>(); //하위 옵션

}
