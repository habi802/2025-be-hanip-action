package kr.co.hanipaction.application.contact;


import jakarta.validation.Valid;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.application.contact.model.*;

import kr.co.hanipaction.configuration.model.SignedUser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Setter
@RequestMapping("/api/contact")
public class ContactController {
    private final ContactService contactService;

    //문의 글 등록
    @PostMapping
    public ResultResponse<?> save(@AuthenticationPrincipal SignedUser signedUser, @Valid @RequestPart ContactPostReq req, @RequestPart(name = "pic") List<MultipartFile> pics){
        long userId= signedUser.signedUserId;

        ContactPostRes result = contactService.save(userId,req,pics);

        if(result ==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("문의글 작성에 실패하였습니다."));

        }
        if(pics.size() > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST
                    , String.format("사진은 %d장까지 선택 가능합니다.", 5));
        }


        return new ResultResponse<>("문의글 등록 완료",result);

    }
    //문의 전체 조회
    // 한 페이지에 10 게시글씩
    @GetMapping
    public ResultResponse<?> getContacts(@AuthenticationPrincipal SignedUser signedUser, @Valid @ModelAttribute ContactGetReq req){
        long userId = signedUser.signedUserId;

        if(req.getPage()<0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("페이지 조회에 실패하였습니다."));

        }

        if(req.getRowPerPage()<10 ||req.getRowPerPage()>=11){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("페이지 조회에 실패하였습니다."));

        }


        List<ContactGetRes> contacts = contactService.getContactList(req);
        int currentPage = req.getPage();

        Map<String, Object> response = new HashMap<>();
        response.put("content", contacts);  // 해당 페이지의 데이터
        response.put("currentPage", currentPage);  // 현재 페이지 번호
        response.put("pageSize", req.getRowPerPage());


        return  new ResultResponse<>("문의글 전체 조회 완료",response);

    }
    // 유저의 문의글 1개를 조회한다
    @GetMapping("/{userId}")
    public ResultResponse<?> getContactsByUserId (@AuthenticationPrincipal SignedUser signedUser, @Valid @ModelAttribute ContactGetReq req, @PathVariable("user_id") long userId){

        return null;
    }

}
