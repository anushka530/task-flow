package taskflow_backend.dto;

import jakarta.validation.constraints.Size;
import taskflow_backend.enums.Priority;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TaskRequestDto {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100)
    private String title;

    private String description;

    private Priority priority;
}
