package taskflow_backend.service;

import org.springframework.data.domain.Page;
import taskflow_backend.dto.TaskRequestDto;
import taskflow_backend.dto.TaskResponseDto;

public interface TaskService {

    TaskResponseDto createTask(TaskRequestDto dto);

    TaskResponseDto getTaskById(Long id);

    TaskResponseDto updateTask(Long id, TaskRequestDto dto);

    void deleteTask(Long id);

    Page<TaskResponseDto> getTasks(int page, int size);

    Page<TaskResponseDto> searchTasks(
            String keyword,
            int page,
            int size);
}