package kr.co.hanipaction.application.menu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MenuDeleteReq {
    private long menuId;
    private long userId;
}
