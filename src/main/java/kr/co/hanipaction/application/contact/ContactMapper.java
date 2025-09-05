package kr.co.hanipaction.application.contact;

import kr.co.hanipaction.application.contact.model.ContactGetDto;
import kr.co.hanipaction.application.contact.model.ContactGetRes;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContactMapper {
    List<ContactGetRes> findAllContact(ContactGetDto dto);
}
