package com.habitask.repository;

import com.habitask.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserTaskRepository extends JpaRepository<Task, Long> {

    @Query("SELECT t FROM Task t JOIN User u ON t.idUserCreator = u.id " +
            "WHERE u.name LIKE %:name% AND u.email LIKE %:email%")
    Page<Task> findByUserUserNameContainingAndUserUserEmailContaining(
            @Param("name") String name,
            @Param("email") String email,
            Pageable pageable);
}
