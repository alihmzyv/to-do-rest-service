package com.alihmzyv.todorestservice.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "task", schema = "public", indexes = {
        @Index(name = "task_name_uindex", columnList = "name", unique = true)
})
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    Integer id;

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    String name;

    @NotNull
    @Column(name = "deadline", nullable = false)
    LocalDate deadline;

    @NotNull
    @Column(name = "description", nullable = false, length = Integer.MAX_VALUE)
    String description;

    @Column(name = "img")
    byte[] img;

    @NotNull
    @Column(name = "done", nullable = false)
    Boolean done = false;

    @NotNull
    @Column(name = "archived", nullable = false)
    Boolean archived = false;

    @NotNull
    @Column(name = "important", nullable = false)
    Boolean important = false;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    User user;

}