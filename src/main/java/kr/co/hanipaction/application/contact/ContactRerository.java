package kr.co.hanipaction.application.contact;

import kr.co.hanipaction.application.contact.model.ContactGetRes;
import kr.co.hanipaction.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ContactRerository extends JpaRepository<Contact, Long> {
    Contact save(long userId);

}
