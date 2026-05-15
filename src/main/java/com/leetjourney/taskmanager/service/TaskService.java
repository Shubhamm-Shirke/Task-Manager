package com.leetjourney.taskmanager.service;

import com.leetjourney.taskmanager.config.TaskSpecifications;
import com.leetjourney.taskmanager.dto.TaskRequest;
import com.leetjourney.taskmanager.dto.TaskResponse;
import com.leetjourney.taskmanager.entity.Task;
import com.leetjourney.taskmanager.exception.TaskAlreadyExistException;
import com.leetjourney.taskmanager.exception.TaskNotFoundException;
import com.leetjourney.taskmanager.mapper.TaskMapper;
import com.leetjourney.taskmanager.repository.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    // ── Paginated search (used by /search endpoint) ──────────────────────────

    @Cacheable(
            value = "searchTasks",
            key = "#title + '-' + #taskStatus + '-' + #page + '-' + #size + '-' + #sortBy + '-' + #sortDir"
    )
    public Map<String, Object> searchTasks(String title, String categoryName,
                                           Boolean taskStatus,
                                           int page, int size,
                                           String sortBy, String sortDir) {

        int pageNumber = (page > 0) ? page - 1 : 0;
        Pageable pageable = buildPageable(pageNumber, size, sortBy, sortDir);
        Specification<Task> spec = TaskSpecifications.withFilters(title,categoryName,taskStatus);
//        Page<Task> taskPage = fetchTaskPage(title, taskStatus, pageable);
        Page<Task> taskPage = taskRepository.findAll(spec,pageable)     ;
        return buildPagedResponse(taskPage);
    }

    @Cacheable(
            value = "allTasks",
            key = "#page + '-' + #size + '-' + #sortBy + '-' + #sortDir"
    )
    public Map<String, Object> getAllTasks(int page, int size,
                                           String sortBy, String sortDir) {

        int pageNumber = (page > 0) ? page - 1 : 0;
        Pageable pageable = buildPageable(pageNumber, size, sortBy, sortDir);
        Page<Task> taskPage = taskRepository.findAll(pageable);
        return buildPagedResponse(taskPage);
    }

    // ── Single-task operations ────────────────────────────────────────────────

    @Cacheable(value = "singleTask", key = "#id")
    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toResponse(task);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "allTasks", allEntries = true),
                    @CacheEvict(value = "searchTasks", allEntries = true),
            }
    )
    public TaskResponse createTask(TaskRequest request) {
        taskRepository.findByTitle(request.getTitle())
                .ifPresent(exist -> {
                    throw new TaskAlreadyExistException(request.getTitle());
                });
        Task saved = taskRepository.save(taskMapper.toEntity(request));
        return taskMapper.toResponse(saved);
    }

    @Caching(
            put = {
                    @CachePut(value = "singleTask", key = "#id"),
            },
            evict = {
                    @CacheEvict(value = "allTasks", allEntries = true),
                    @CacheEvict(value = "searchTasks", allEntries = true),
            }
    )
    public TaskResponse updateTask(Long id, TaskRequest request) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskMapper.updateEntityFromRequest(task, request);
        return taskMapper.toResponse(taskRepository.save(task));
    }


    @Caching(
            evict = {
                    @CacheEvict(value = "singleTask", key = "#id"),
                    @CacheEvict(value = "allTasks", allEntries = true),
                    @CacheEvict(value = "searchTasks", allEntries = true)
            }
    )
    public String deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(task);
        return "Task Deleted Successfully.";
    }

    // ── List queries (used by legacy list endpoints) ──────────────────────────

    public List<TaskResponse> getTasksByCompletionStatus(boolean status) {
        return taskRepository.findByTaskStatus(status).stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    public List<TaskResponse> searchTasksByTitle(String title) {
        return taskRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private Pageable buildPageable(int page, int size, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

//    private Page<Task> fetchTaskPage(String title, Boolean taskStatus, Pageable pageable) {
//        if (title != null && taskStatus != null) {
//            return taskRepository.findByTitleContainingAndTaskStatus(title, taskStatus, pageable);
//        } else if (title != null) {
//            return taskRepository.findByTitleContainingIgnoreCase(title, pageable);
//        } else if (taskStatus != null) {
//            return taskRepository.findByTaskStatus(taskStatus, pageable);
//        } else {
//            return taskRepository.findAll(pageable);
//        }
//    }

    private Map<String, Object> buildPagedResponse(Page<Task> taskPage) {

//        List<TaskResponse> tasks = taskPage.getContent().stream()
//                .map(task -> TaskResponse.builder()
//                        .id(task.getId())
//                        .title(task.getTitle())
//                        .description(task.getDescription())
//                        .taskStatus(task.getTaskStatus())
//                        .createdAt(task.getCreatedAt())
//                        .category(
//                                CategoryResponse.builder()
//                                        .categoryId(task.getCategory().getId())
//                                        .name(task.getCategory().getName())
//                                        .description(task.getCategory().getDescription())
//                                        .build()
//                        )
//                        .build())
//                .toList();

        List<TaskResponse> tasks = taskPage.getContent().stream()
                .map(taskMapper::toResponse)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("tasks", tasks);
        response.put("currentPage", taskPage.getNumber() + 1);
        response.put("totalItems", taskPage.getTotalElements());
        response.put("totalPages", taskPage.getTotalPages());
        response.put("hasNext", taskPage.hasNext());
        response.put("hasPrevious", taskPage.hasPrevious());
        response.put("isLastPage", taskPage.isLast());
        response.put("isFirstPage", taskPage.isFirst());

        return response;
    }
}