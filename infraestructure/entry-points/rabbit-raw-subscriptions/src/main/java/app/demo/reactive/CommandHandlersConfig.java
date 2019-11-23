package app.demo.reactive;

import lombok.Data;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.rabbitmq.RabbitFlux;
import reactor.rabbitmq.Receiver;
import reactor.rabbitmq.ReceiverOptions;

import org.reactivecommons.async.impl.config.annotations.EnableMessageListeners;
import app.demo.usecase.todo.AssignTasksUseCase;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableMessageListeners
public class CommandHandlersConfig {

    static final String ASSIGN_TASK_COMMAND = "tasks.task.assign";
    static final String COMPLETE_TASK_COMMAND = "tasks.task.complete";

    private final AssignTasksUseCase useCase;

    public CommandHandlersConfig(AssignTasksUseCase usecase) {
        this.useCase = usecase;
    }

    public void init() {
        ReceiverOptions options = new ReceiverOptions();
        Receiver receiver = RabbitFlux.createReceiver(options);
        receiver.consumeManualAck("queue").flatMap(msg -> {
            byte[] body = msg.getBody();
            // .....
            return useCase.assignTask("taskId", "userId").doOnSuccess(x -> msg.ack()).then(Mono.just(""));
        }).subscribeOn(Schedulers.elastic()).subscribe();
    }

    @Data
    static class AssignTaskData {
        private String taskId;
        private String userId;
    }

}
