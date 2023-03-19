package com.alihmzyv.todorestservice.repo;

import com.alihmzyv.todorestservice.model.entity.QTask;
import com.alihmzyv.todorestservice.model.entity.Task;
import com.querydsl.core.types.dsl.StringPath;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository
        extends JpaRepository<Task, Integer>, QuerydslPredicateExecutor<Task>, QuerydslBinderCustomizer<QTask> {
    @Override
    default void customize(QuerydslBindings bindings, @NotNull QTask root) {
        bindings.bind(String.class).first(
                (StringPath path, String value) -> path.containsIgnoreCase(value));
    }

    Optional<Task> findByUserIdAndId(Integer userId, Integer taskId);

    boolean existsByName(String name);
}