package ir.alirezaalijani.security.authorization.service.repository;

import ir.alirezaalijani.security.authorization.service.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    @Query("select u.id from User as u where u.username = :username")
    Optional<Integer> findIdByUsername(@Param("username") String username);

    Optional<User> findByUsernameOrEmail(String value1,String value2);

    @Modifying
    @Query("update User as u set u.lastLogin = :d where u.username= :u")
    @Transactional(rollbackFor=Exception.class)
    void updateUserLastLogin(@Param("u") String username ,@Param("d") Date newLoginDate);
    @Modifying
    @Query("update User as u set u.password = :p where u.username= :u")
    @Transactional(rollbackFor=Exception.class)
    void updateUserPassword(@Param("u") String username ,@Param("p") String password);

    Boolean existsByUsernameOrEmail(String username,String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    Boolean existsByEmailAndEmailVerification(String email,boolean verification);
    Boolean existsByUsernameAndEmailVerificationAndEnable(String username,boolean emailV,boolean enable);

    void deleteByUsername(String username);
}
