package app.demo.mongodb.todo;

import app.demo.domain.todo.TaskToDo;
import app.demo.domain.todo.gateway.TaskToDoRepository;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import app.demo.reactive.repository.mongo.AdapterOperations;

@Repository
public class TaskToDoRepositoryAdapter extends AdapterOperations<TaskToDo, TaskToDoData, String, TaskToDoDataRepository> implements TaskToDoRepository {

    @Autowired
    public TaskToDoRepositoryAdapter(TaskToDoDataRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.mapBuilder(d, TaskToDo.TaskToDoBuilder.class).build());
    }

    @Override
    public Mono<Void> saveAll(Flux<TaskToDo> tasks) {
        return repository.saveAll(tasks.map(this::toData)).then();
    }

    @Override
    public Flux<TaskToDo> findAll() {
        return doQueryMany(repository.findAll());
    }

    @Override
    public Flux<TaskToDo> findAllUserOpenTasks(String userId) {
        return doQueryMany(repository.findAllByAssignedUserIdAndDone(userId, false));
    }
}
