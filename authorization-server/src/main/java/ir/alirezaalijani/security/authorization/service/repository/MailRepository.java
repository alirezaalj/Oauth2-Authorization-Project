package ir.alirezaalijani.security.authorization.service.repository;

import ir.alirezaalijani.security.authorization.service.repository.model.ApplicationMail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<ApplicationMail,Integer> {
}
