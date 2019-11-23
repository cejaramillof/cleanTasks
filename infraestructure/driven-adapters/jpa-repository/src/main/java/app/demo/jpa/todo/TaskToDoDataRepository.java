package app.demo.jpa.todo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import java.util.List;

public interface TaskToDoDataRepository extends CrudRepository<TaskToDoData, String>, QueryByExampleExecutor<TaskToDoData> {

    List<TaskToDoData> findAllByAssignedUserIdAndDone(String userId, Boolean done);
}
