package app.demo.jpa.todo;

import app.demo.domain.todo.TaskToDo;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class TaskToDoData {
    @Id
    private String id;
    private String name;
    private String description;
    private Date doneDate;
    private String assignedUserId;
    private TaskToDo.TaskReportStatus reportStatus;
    private boolean done;
}
