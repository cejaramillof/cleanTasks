package app.demo.mongodb.todo;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

interface TaskToDoDataRepository extends ReactiveCrudRepository<TaskToDoData, String>, ReactiveQueryByExampleExecutor<TaskToDoData> {

    Flux<TaskToDoData> findAllByAssignedUserIdAndDone(String userId, Boolean done);
}
