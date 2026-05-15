package com.leetjourney.taskmanager.repository;

import com.leetjourney.taskmanager.entity.Task;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

 @Repository
public interface TaskRepository extends JpaRepository<Task, Long> , JpaSpecificationExecutor<Task> {

    // Method Name Query
    // SELECT * FROM tasks WHERE taskStatus = :true/false/0/1
    List<Task> findByTaskStatus(boolean taskStatus);

    List<Task> findByTitleContainingIgnoreCase(String title);

    @Query("SELECT t FROM Task t WHERE t.taskStatus = :taskStatus")
    List<Task> findTasksByCompletionStatus(@Param("taskStatus") boolean taskStatus);

    // New paginated methods
    Page<Task> findByTaskStatus(boolean taskStatus, Pageable pageable);
    Page<Task> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT t FROM Task t WHERE t.taskStatus = :taskStatus")
    Page<Task> findTasksByCompletionStatus(@Param("taskStatus") boolean taskStatus,
                                           Pageable pageable);

    @Query("SELECT t " +
            "FROM Task t " +
            "WHERE LOWER(t.title) LIKE " +
            "LOWER(CONCAT('%', :title, '%')) AND t.taskStatus = :taskStatus")
    Page<Task> findByTitleContainingAndTaskStatus(String title,
                                                 boolean taskStatus,
                                                 Pageable pageable);

    Optional<Task> findByTitle(String title);

     @EntityGraph(attributePaths = "category")
     Page<Task> findAll(Pageable pageable);

     @EntityGraph(attributePaths = "category")
     Page<Task> findAll(Specification specification, Pageable pageable);
}
