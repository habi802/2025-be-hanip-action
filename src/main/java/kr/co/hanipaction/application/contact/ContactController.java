package kr.co.hanipaction.application.contact;


import jakarta.validation.Valid;
import kr.co.hanipaction.application.contact.model.*;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.configuration.model.SignedUser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
        long userId=signedUser.signedUserId;

        ContactPostRes result = contactService.save(userId,req,pics);
        return new ResultResponse<>("문의글 등록 완료",result);

    }
    //문의 전체 조회
    // 한 페이지에 10 게시글씩
    @GetMapping
    public ResultResponse<?> getContacts(@AuthenticationPrincipal SignedUser signedUser, @Valid @ModelAttribute ContactGetReq req){
        long userId = signedUser.signedUserId;



        List<ContactGetRes> contacts = contactService.getContactList(req);
        int currentPage = req.getPage();

        Map<String, Object> response = new HashMap<>();
        response.put("content", contacts);  // 해당 페이지의 데이터
        response.put("currentPage", currentPage);  // 현재 페이지 번호
        response.put("pageSize", req.getRowPerPage());


        return  new ResultResponse<>("문의글 전체 조회 완료",response);

    }



}
