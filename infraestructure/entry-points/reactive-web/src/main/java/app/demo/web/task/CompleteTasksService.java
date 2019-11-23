package app.demo.web.task;

import app.demo.usecase.todo.CompleteTasksUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CompleteTasksService {

    private final CompleteTasksUseCase useCase;

    @PostMapping(path = "/task/{id}/complete")
    public Mono<Void> completeTask(@PathVariable("id") String taskId) {
        return useCase.markAsDone(taskId);
    }

}
