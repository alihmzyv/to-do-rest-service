package com.alihmzyv.todorestservice.config.init;

import com.alihmzyv.todorestservice.model.entity.Role;
import com.alihmzyv.todorestservice.repo.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@RequiredArgsConstructor
@Component
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) {
        Role role = Role.builder()
                .name("ROLE_USER")
                .build();
        roleRepository.save(role);
    }
}
