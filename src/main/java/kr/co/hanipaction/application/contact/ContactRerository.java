package kr.co.hanipaction.application.contact;

import kr.co.hanipaction.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRerository extends JpaRepository<Contact, Long> {
    Contact save(long userId);
}
