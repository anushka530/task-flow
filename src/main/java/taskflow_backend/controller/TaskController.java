package taskflow_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import taskflow_backend.dto.ApiResponse;
import taskflow_backend.dto.TaskRequestDto;
import taskflow_backend.dto.TaskResponseDto;
import taskflow_backend.service.TaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskResponseDto>>
    createTask(
            @Valid @RequestBody TaskRequestDto dto) {

        TaskResponseDto task =
                taskService.createTask(dto);

        ApiResponse<TaskResponseDto> response =
                ApiResponse.<TaskResponseDto>builder()
                        .success(true)
                        .message("Task created successfully")
                        .data(task)
                        .timestamp(LocalDateTime.now())
                        .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDto>>
    getTaskById(
            @PathVariable Long id) {

        TaskResponseDto task =
                taskService.getTaskById(id);

        ApiResponse<TaskResponseDto> response =
                ApiResponse.<TaskResponseDto>builder()
                        .success(true)
                        .message("Task fetched successfully")
                        .data(task)
                        .timestamp(LocalDateTime.now())
                        .build();

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponseDto>>
    updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequestDto dto) {

        TaskResponseDto updatedTask =
                taskService.updateTask(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<TaskResponseDto>builder()
                        .success(true)
                        .message("Task updated successfully")
                        .data(updatedTask)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}")

    @PreAuthorize(
            "hasRole('ADMIN')"
    )
    public ResponseEntity<ApiResponse<Object>>
    deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.ok(
                ApiResponse.builder()
                        .success(true)
                        .message("Task deleted successfully")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<TaskResponseDto>>>
    getTasks(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        Page<TaskResponseDto> tasks =
                taskService.getTasks(page, size);

        return ResponseEntity.ok(
                ApiResponse.<Page<TaskResponseDto>>builder()
                        .success(true)
                        .message("Tasks fetched")
                        .data(tasks)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<TaskResponseDto>>>
    searchTasks(

            @RequestParam String keyword,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        Page<TaskResponseDto> result =
                taskService.searchTasks(
                        keyword,
                        page,
                        size);

        return ResponseEntity.ok(
                ApiResponse.<Page<TaskResponseDto>>builder()
                        .success(true)
                        .message("Search successful")
                        .data(result)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}