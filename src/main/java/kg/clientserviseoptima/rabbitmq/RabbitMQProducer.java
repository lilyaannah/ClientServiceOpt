package kg.clientserviseoptima.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RabbitMQProducer {

    private final RabbitTemplate rabbitTemplate;


    public void sendEvent(String exchange, Object event) {
        rabbitTemplate.convertAndSend(exchange, "my-routing-key", event);
    }

}