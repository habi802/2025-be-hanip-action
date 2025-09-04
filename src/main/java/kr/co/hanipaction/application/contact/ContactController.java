package kr.co.hanipaction.application.contact;


import jakarta.validation.Valid;
import kr.co.hanipaction.application.contact.model.ContactPostReq;
import kr.co.hanipaction.application.contact.model.ContactPostRes;
import kr.co.hanipaction.configuration.model.ResultResponse;
import kr.co.hanipaction.configuration.model.SignedUser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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



}
