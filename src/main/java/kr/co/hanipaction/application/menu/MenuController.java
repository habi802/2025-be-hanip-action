package kr.co.hanipaction.application.menu;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.hanipaction.application.common.model.ResultResponse;
import kr.co.hanipaction.application.common.util.HttpUtils;
import kr.co.hanipaction.application.menu.model.*;
import kr.co.hanipaction.application.user.etc.UserConstants;
import kr.co.hanipaction.configuration.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Slf4j
@RequestMapping("/api/menu")
@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

//    메뉴 올리기
    @PostMapping
    public ResultResponse<Integer> menuPosting(@RequestPart MultipartFile img, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestPart MenuPostReq data){
//        int logginedMemberId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);

        int result = menuService.memoPosting(img,data,userPrincipal.getSignedUserId());
        log.info(data.toString());
        if(result == 0){
            return ResultResponse.fail(400,"메뉴 등록 실패");
        }
        return ResultResponse.success(result);
    }
// 가게 메뉴 리스트 조회
    @GetMapping("/{storeId}")
    public ResultResponse<List<MenuGetListRes>> menuGetting(@PathVariable long storeId) {
        List<MenuGetListRes> result = menuService.menuGetList(storeId);
        if(result == null || result.size() == 0){
            return ResultResponse.fail(400,"메뉴 리스트 조회 실패");
        }


        return ResultResponse.success(result);
    }

    // 로그인 세션 가게 메뉴 조회
    @GetMapping("/owner")
    public ResultResponse<List<MenuGetListRes>> menuGetting(@AuthenticationPrincipal UserPrincipal userPrincipal) {
//        int logginedMemberId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        List<MenuGetListRes> result = menuService.findByUserId(userPrincipal.getSignedUserId());
        if(result == null || result.size() == 0){
            return ResultResponse.fail(400,"메뉴 리스트 조회 실패");
        }


        return ResultResponse.success(result);
    }

//    가게 메뉴 1개 조회
    @GetMapping
    public ResultResponse<MenuGetRes> menuOneGetting(@RequestParam long menuId) {
        MenuGetRes result = menuService.menuGetOne(menuId);

        if(result == null){
            return ResultResponse.fail(400,"메뉴 조회 실패");
        }

        return ResultResponse.success(result);
    }
    
    //가게 메뉴 수정
    @PutMapping
    public ResultResponse<Integer> memoPutting(@RequestPart(required = false) MultipartFile img, @RequestPart MenuPutReq data, @AuthenticationPrincipal UserPrincipal userPrincipal){
//        int logginedMemberId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        int result = menuService.menuPut(img,data,userPrincipal.getSignedUserId());
        log.info(data.toString());
        if(result == 0){
            return ResultResponse.fail(400,"메뉴 수정 실패");
        }

        return  ResultResponse.success(result);
    }
    
    //메뉴 삭제
    @DeleteMapping("/{menuId}")
    public ResultResponse<Integer> memoDeleting(@AuthenticationPrincipal UserPrincipal userPrincipal,@PathVariable int menuId) {
//        int logginedMemberId = (int) HttpUtils.getSessionValue(httpReq, UserConstants.LOGGED_IN_USER_ID);
        MenuDeleteReq req = new MenuDeleteReq(menuId,userPrincipal.getSignedUserId());
        int result = menuService.menuDelete(req);
        if(result == 0 || userPrincipal.getSignedUserId() == 0 ){
            return ResultResponse.fail(400,"메뉴 삭제 실패");
        }

        return  ResultResponse.success(result);
    }

}
