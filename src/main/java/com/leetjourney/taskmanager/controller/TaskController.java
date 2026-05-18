package com.leetjourney.taskmanager.controller;

import com.leetjourney.taskmanager.dto.FileUploadResponse;
import com.leetjourney.taskmanager.dto.TaskRequest;
import com.leetjourney.taskmanager.dto.TaskResponse;
import com.leetjourney.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.unit.DataSize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

//    @Value("${file.size}")
//    private DataSize maxSize;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchTasks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) Boolean taskStatus,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        return ResponseEntity.ok(
                taskService.searchTasks(title, categoryName, taskStatus, page, size, sortBy, sortDir)
        );
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllTasks(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDir) {

        return ResponseEntity.ok(
                taskService.getAllTasks(page, size, sortBy, sortDir)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
//        return ResponseEntity.status(HttpStatus.OK).body(taskService.getTaskById(id));
//        return ResponseEntity.ok(taskService.getTaskById(id));
        return new ResponseEntity<>(taskService.getTaskById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest task) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task));

    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest updatedTask) {
        return ResponseEntity.ok(taskService.updateTask(id, updatedTask));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.deleteTask(id));
    }

    @GetMapping("/taskStatus/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByCompletions(@PathVariable boolean status) {
        return ResponseEntity.ok(taskService.getTasksByCompletionStatus(status));
    }

    @GetMapping("/search-by-title")
    public ResponseEntity<List<TaskResponse>> searchTasksByTitle(@RequestParam String title) {
        return ResponseEntity.ok(taskService.searchTasksByTitle(title));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(file.getOriginalFilename() + " File is empty");
        }

        if (file.getSize() > 4*1024*1024) {
            return ResponseEntity.badRequest().body("Max size of file is 4 MB");
        }

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || !originalFileName.endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Only CSV format accepted");
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + originalFileName;

        String uploadDir = "uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdir();
        }

        Path filePath = Paths.get(uploadDir).resolve(uniqueFileName);
        Files.write(filePath, file.getBytes());

        return ResponseEntity.ok(taskService.importTaskFile(file));
    }

}
