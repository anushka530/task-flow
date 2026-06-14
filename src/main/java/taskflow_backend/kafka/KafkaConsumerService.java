package taskflow_backend.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @KafkaListener(
            topics = "task-created",
            groupId = "taskflow-group"
    )
    public void consume(
            String message) {

        System.out.println(
                "Received Event -> "
                        + message
        );
    }
}