package kr.co.hanipaction.openfeign.menu.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class MenuGetReq {
    private List<Long> menuIds;
    private List<Long> optionIds;
}
