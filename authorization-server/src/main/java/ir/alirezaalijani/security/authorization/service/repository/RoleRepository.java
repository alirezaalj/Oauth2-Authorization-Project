package ir.alirezaalijani.security.authorization.service.repository;

import ir.alirezaalijani.security.authorization.service.repository.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByName(String name);
}
