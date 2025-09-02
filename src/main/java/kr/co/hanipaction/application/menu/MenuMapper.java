package kr.co.hanipaction.application.menu;

import kr.co.hanipaction.application.menu.model.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MenuMapper {
    int menuPost(MenuPostReq req);
    int menuModify(MenuPutReq req);
    int menuDelete(MenuDeleteReq req);
    List<MenuGetListRes> menuGetList(long storeId);
    MenuGetRes menuGetOne(long menuId);
    List<MenuGetListRes> findByUserId(long userId);

}
