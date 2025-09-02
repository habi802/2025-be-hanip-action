package kr.co.hanipaction.application.menu;

import kr.co.hanipaction.application.common.util.MyFileUtils;
import kr.co.hanipaction.application.menu.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuMapper menuMapper;
    private final MyFileUtils myFileUtils;

    public int memoPosting(MultipartFile img, MenuPostReq req, long logginedMemberId){
//        MenuPostReq req2 = new MenuPostReq();
//        req2.setStoreId();
        String savedMenuFileName = myFileUtils.makeRandomFileName(img);
        req.setImagePath(savedMenuFileName);
        req.setUserId(logginedMemberId);
        int result = menuMapper.menuPost(req);
        String directoryPath = String.format("/menu-profile/%d",req.getId());
        myFileUtils.makeFolders(directoryPath);

        String savePathFileName = directoryPath + "/" + savedMenuFileName;
        try{
            myFileUtils.transferTo(img,savePathFileName);
        } catch(Exception e){
            e.printStackTrace();
            return 0;
        }


        return 1;
    }

    public List<MenuGetListRes> menuGetList(long storeId){
        return menuMapper.menuGetList(storeId);
    }
    public MenuGetRes menuGetOne(long menuId){
        return menuMapper.menuGetOne(menuId);
    }
    public int menuPut(MultipartFile img, MenuPutReq req, long logginedMemberId){
        String savedMenuFileName = null;
        if(img != null && !img.isEmpty()) {
            savedMenuFileName = myFileUtils.makeRandomFileName(img);
            String directoryPath = String.format("/menu-profile/%d",req.getId());
            myFileUtils.makeFolders(directoryPath);

            String savePathFileName = directoryPath + "/" + savedMenuFileName;
            try{
                myFileUtils.transferTo(img,savePathFileName);
            } catch(Exception e){
                e.printStackTrace();
                return 0;
            }
        }
        req.setImagePath(savedMenuFileName);
        req.setUserId(logginedMemberId);

        int result = menuMapper.menuModify(req);
        return result;
    }
    public int menuDelete(MenuDeleteReq req){
        return menuMapper.menuDelete(req);
    }

    public List<MenuGetListRes> findByUserId(long userId){
        return menuMapper.findByUserId(userId);
    }

}
