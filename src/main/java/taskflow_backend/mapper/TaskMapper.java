package taskflow_backend.mapper;

import org.springframework.stereotype.Component;
import taskflow_backend.dto.TaskResponseDto;
import taskflow_backend.entity.Task;

@Component
public class TaskMapper {

    public TaskResponseDto toDto(Task task){

        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority().name())
                .status(task.getStatus().name())
                .build();
    }
}