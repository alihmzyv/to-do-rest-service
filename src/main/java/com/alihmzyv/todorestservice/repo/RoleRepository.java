package com.alihmzyv.todorestservice.repo;

import com.alihmzyv.todorestservice.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
