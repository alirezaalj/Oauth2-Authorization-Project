package ir.alirezaalijani.security.authorization.service.repository;

import ir.alirezaalijani.security.authorization.service.repository.model.ApplicationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TokenRepository extends JpaRepository<ApplicationToken,String> {
    Boolean existsByIdAndExpired(String id,Boolean expired);
    @Modifying
    @Query("update ApplicationToken as t set t.expired = true, t.useTime = current_date, t.updateAt= current_date where t.id = :id")
    @Transactional(rollbackFor=Exception.class)
    void updateTokenUsed(@Param("id") String id);

}
