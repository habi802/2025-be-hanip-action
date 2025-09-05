package kr.co.hanipaction.application.contact;

import jakarta.transaction.Transactional;
import kr.co.hanipaction.application.contact.model.*;
import kr.co.hanipaction.configuration.utill.MyFileManager;
import kr.co.hanipaction.entity.Contact;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactService {
    @Autowired
    private final ContactRerository contactRerository;
    private final MyFileManager myFileManager;
    private final ContactMapper contactMapper;


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

    public List<ContactGetRes> getContactList(ContactGetReq req){
        long startIdx = (req.getPage() - 1) * req.getRowPerPage(); // 시작 인덱스 계산
        long size = req.getRowPerPage();

        ContactGetDto dto = new ContactGetDto(startIdx, size, req.getUserId());


        return   contactMapper.findAllContact(dto);

    }
//    public ContactGetRes getContactListByUserId(long contactId){
//        Contact contact = Contact.builder()
//                .id(contactId)
//                .build();
//
//       return contactRerository.findContactId(contact.getId());
//
//
//    }
}
