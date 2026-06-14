package taskflow_backend.kafka;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCreatedEvent {

    private Long taskId;

    private String title;
}