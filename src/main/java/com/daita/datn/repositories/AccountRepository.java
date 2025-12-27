package com.daita.datn.repositories;

import com.daita.datn.enums.RoleType;
import com.daita.datn.models.entities.auth.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    Optional<Account> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByRoles_RoleName(RoleType roleType);
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.roles WHERE a.email = :email")
    Optional<Account> findByEmailWithRoles(@Param("email") String email);
}
