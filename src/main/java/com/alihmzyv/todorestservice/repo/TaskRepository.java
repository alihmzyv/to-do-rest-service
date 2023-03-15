package com.alihmzyv.todorestservice.repo;

import com.alihmzyv.todorestservice.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByAppUserIdAndId(Integer userId, Integer taskId);
}