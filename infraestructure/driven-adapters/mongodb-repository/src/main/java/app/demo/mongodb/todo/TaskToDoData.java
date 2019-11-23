package app.demo.mongodb.todo;

import app.demo.domain.todo.TaskToDo;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document
@NoArgsConstructor
class TaskToDoData {
    @Id
    private String id;
    private String name;
    private String description;
    private Date doneDate;
    private boolean done;
    private String assignedUserId;
    private TaskToDo.TaskReportStatus reportStatus;
}
