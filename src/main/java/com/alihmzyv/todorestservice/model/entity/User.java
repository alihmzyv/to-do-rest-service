package com.alihmzyv.todorestservice.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "users", schema = "public", indexes = {
        @Index(name = "users_email_address_uindex", columnList = "email_address", unique = true)
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @NotNull
    @Column(name = "first_name", nullable = false, length = Integer.MAX_VALUE)
    String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false, length = Integer.MAX_VALUE)
    String lastName;

    @NotNull
    @Column(name = "email_address", nullable = false, length = Integer.MAX_VALUE)
    String emailAddress;

    @NotNull
    @Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
    String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @ToString.Exclude
    Set<Task> tasks = new LinkedHashSet<>();

}