package com.alihmzyv.todorestservice.service.impl;

import com.alihmzyv.todorestservice.config.i18n.MessageSource;
import com.alihmzyv.todorestservice.exception.ActionNotAllowedException;
import com.alihmzyv.todorestservice.exception.DuplicateNotAllowedException;
import com.alihmzyv.todorestservice.exception.UserNotFoundException;
import com.alihmzyv.todorestservice.mapper.UserMapper;
import com.alihmzyv.todorestservice.model.dto.user.ForgotPasswordDto;
import com.alihmzyv.todorestservice.model.dto.user.RegisterUserDto;
import com.alihmzyv.todorestservice.model.dto.user.ResetPasswordDto;
import com.alihmzyv.todorestservice.model.dto.user.UserRespDto;
import com.alihmzyv.todorestservice.model.entity.AppUser;
import com.alihmzyv.todorestservice.model.entity.Role;
import com.alihmzyv.todorestservice.repo.RoleRepository;
import com.alihmzyv.todorestservice.repo.UserRepository;
import com.alihmzyv.todorestservice.service.PasswordResetService;
import com.alihmzyv.todorestservice.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepo;
    private final UserMapper userMapper;
    private final RoleRepository roleRepo;
    private final EmailService emailService;
    private final MessageSource messageSource;
    private final PasswordResetService passwordResetService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Integer createUser(RegisterUserDto registerUserDto) {
        requiresUserDoesNotExistByEmailAddress(registerUserDto.getEmailAddress());
        AppUser user = userMapper.registerUserDtoToUser(registerUserDto);
        user.setRoles(Collections.singleton(roleRepo.findByName("ROLE_USER")));
        userRepo.save(user);
        return user.getId();
    }

    @Override
    public UserRespDto getUserRespDtoById(Integer userId) {
        return userRepo.findById(userId)
                .map(userMapper::userToUserRespDto)
                .orElseThrow(() -> new UserNotFoundException(String.format("%s: %d",
                        messageSource.getMessage("user.id.not.found"),
                        userId)));
    }

    @Override
    public AppUser findUserById(Integer userId) {
        return userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.format("%s: %d",
                        messageSource.getMessage("user.id.not.found"),
                        userId)));
    }

    @Override
    public AppUser findUserByEmailAddress(String emailAddress) {
        return userRepo.findByEmailAddress(emailAddress)
                .orElseThrow(() ->
                        new UserNotFoundException(String.format(
                                "%s: %s",
                                messageSource.getMessage("user.email.not.found"),
                                emailAddress)));
    }

    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        return userRepo.findByEmailAddress(emailAddress)
                .map(user -> {
                    List<SimpleGrantedAuthority> authorities = user.getRoles()
                            .stream()
                            .map(Role::getName)
                            .map(SimpleGrantedAuthority::new)
                            .toList();
                    return User.builder()
                            .username(user.getEmailAddress())
                            .password(user.getPassword())
                            .authorities(authorities)
                            .build();
                })
                .orElseThrow(() -> new UsernameNotFoundException(String.format("%s: %s",
                        messageSource.getMessage("user.email.not.found"),
                        emailAddress)));
    }

    private void requiresUserDoesNotExistByEmailAddress(String emailAddress) {
        if (userRepo.existsByEmailAddress(emailAddress))
            throw new DuplicateNotAllowedException(String.format(
                    "%s: %s",
                    messageSource.getMessage("user.email.exists"),
                    emailAddress));
    }

    @Override
    public void sendResetPasswordEmail(ForgotPasswordDto forgotPasswordDto) {
        String emailAddress = forgotPasswordDto.getEmailAddress();
        if (userRepo.existsByEmailAddress(emailAddress)) {
            emailService.sendEmail(
                    emailAddress,
                    messageSource.getMessage("user.password.reset.subject"),
                    String.format("%s: %s",
                            messageSource.getMessage("user.password.reset.body"),
                            passwordResetService.putAndGetToken(emailAddress)));
        }
    }

    @Override
    public void resetPassword(String token, ResetPasswordDto resetPasswordDto) {
        passwordResetService.getEmailAddress(token)
                .ifPresentOrElse(
                        emailAddress -> {
                            String encryptedPassword = passwordEncoder.encode(resetPasswordDto.getNewPassword());
                            userRepo.updatePassword(emailAddress, encryptedPassword);
                        },
                        () -> {
                            throw new ActionNotAllowedException(String.format("%s (%d seconds): %s",
                                    messageSource.getMessage("user.password.reset.token.invalid"),
                                    passwordResetService.getExpirationTimeSeconds(),
                                    token));
                        });
    }
}
