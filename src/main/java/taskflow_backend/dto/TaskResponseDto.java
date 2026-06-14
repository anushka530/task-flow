package taskflow_backend.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponseDto implements Serializable {

    private Long id;

    private String title;

    private String description;

    private String priority;

    private String status;
}