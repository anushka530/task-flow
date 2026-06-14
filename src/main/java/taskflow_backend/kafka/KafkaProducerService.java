package taskflow_backend.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, String>
            kafkaTemplate;

    public void publishTaskCreatedEvent(
            String message){

        System.out.println(
                "Publishing Event -> "
                        + message
        );

        kafkaTemplate.send(
                "task-created",
                message
        );
    }


}

