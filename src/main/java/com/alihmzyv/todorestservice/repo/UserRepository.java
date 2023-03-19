package com.alihmzyv.todorestservice.repo;

import com.alihmzyv.todorestservice.model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByEmailAddress(String emailAddress);

    boolean existsByEmailAddress(String emailAddress);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE users SET password = :newPassword WHERE email_address = :emailAddress")
    void updatePassword(String emailAddress, String newPassword);
}