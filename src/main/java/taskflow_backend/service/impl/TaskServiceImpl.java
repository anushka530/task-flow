package taskflow_backend.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import taskflow_backend.audit.AuditLog;
import taskflow_backend.dto.TaskRequestDto;
import taskflow_backend.dto.TaskResponseDto;
import taskflow_backend.entity.Task;
import taskflow_backend.entity.User;
import taskflow_backend.enums.TaskStatus;
import taskflow_backend.exception.ResourceNotFoundException;
import taskflow_backend.kafka.KafkaProducerService;
import taskflow_backend.kafka.TaskCreatedEvent;
import taskflow_backend.mapper.TaskMapper;
import taskflow_backend.repository.AuditLogRepository;
import taskflow_backend.repository.TaskRepository;
import taskflow_backend.repository.UserRepository;
import taskflow_backend.service.TaskService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final AuditLogRepository auditLogRepository;
    private final KafkaProducerService kafkaProducerService;




    @Override
    @Transactional
    public TaskResponseDto createTask(
            TaskRequestDto dto){

        String email =
                getCurrentUserEmail();

        User user =
                userRepository.findByEmail(email)
                        .orElseThrow(
                                () -> new ResourceNotFoundException(
                                        "User not found"
                                )
                        );

        Task task = Task.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .priority(dto.getPriority())
                .status(TaskStatus.PENDING)
                .user(user)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Task savedTask = taskRepository.save(task);

        kafkaProducerService
                .publishTaskCreatedEvent(
                        "Task Created : "
                                + savedTask.getId()
                                + " - "
                                + savedTask.getTitle()
                );

        AuditLog log =
                AuditLog.builder()
                        .action("TASK_CREATED")
                        .userEmail(email)
                        .createdAt(
                                LocalDateTime.now()
                        )
                        .build();

        auditLogRepository.save(log);
        //temporary
//        if (true) {
//            throw new RuntimeException(
//                    "Testing Transaction Rollback"
//            );
//        }

        return taskMapper.toDto(savedTask);
    }

    @Override
    @Cacheable(
            value = "tasks",
            key = "#id" // task 1
    )
    public TaskResponseDto getTaskById(Long id) {

        System.out.println(
                "Fetching from Database..."
        );

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + id));

        return taskMapper.toDto(task);
    }

    @Override
    @CacheEvict(
            value = "tasks",
            key = "#id"
    )
    public TaskResponseDto updateTask(
            Long id,
            TaskRequestDto dto) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found"));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setPriority(dto.getPriority());
        task.setUpdatedAt(LocalDateTime.now());

        Task updatedTask =
                taskRepository.save(task);

        return taskMapper.toDto(updatedTask);
    }

    @Override
    @CacheEvict(
            value = "tasks",
            key = "#id"
    )
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found"));

        taskRepository.delete(task);
    }

    @Override
    public Page<TaskResponseDto> getTasks(
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(
                        page,
                        size,
                        Sort.by("createdAt")
                                .descending()
                );

        Page<Task> taskPage =
                taskRepository.findAll(pageable);

        return taskPage.map(taskMapper::toDto);
    }

    @Override
    public Page<TaskResponseDto> searchTasks(
            String keyword,
            int page,
            int size) {

        Pageable pageable =
                PageRequest.of(page, size);


        Page<Task> taskPage =
                taskRepository.findByTitleContainingIgnoreCase(
                        keyword,
                        pageable
                );

        return taskPage.map(taskMapper::toDto);
    }

    private String getCurrentUserEmail() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        return authentication.getName();
    }
}