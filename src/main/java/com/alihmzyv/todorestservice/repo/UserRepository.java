package com.alihmzyv.todorestservice.repo;

import com.alihmzyv.todorestservice.model.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {
    Optional<AppUser> findByEmailAddress(String emailAddress);
}