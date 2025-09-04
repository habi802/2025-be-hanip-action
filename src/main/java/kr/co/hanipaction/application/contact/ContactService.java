package kr.co.hanipaction.application.contact;

import jakarta.transaction.Transactional;
import kr.co.hanipaction.application.contact.model.ContactPostReq;
import kr.co.hanipaction.application.contact.model.ContactPostRes;
import kr.co.hanipaction.configuration.utill.MyFileManager;
import kr.co.hanipaction.entity.Contact;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRerository contactRerository;
    private final MyFileManager myFileManager;


    @Transactional
    public ContactPostRes save(long signedUserId, ContactPostReq req, List<MultipartFile> pics){

        Contact contact = Contact.builder()
                .userId(signedUserId)
                .title(req.getTitle())
                .userComment(req.getUserComment())
                .build();

        contactRerository.save(contact);

        List<String> fileNames = myFileManager.saveContactPics(contact.getId(),pics);

            contact.addContactPics(fileNames);

        return new ContactPostRes(contact.getId(),fileNames);
    }

}
