package app.demo.web.task;

import app.demo.domain.todo.TaskToDo;
import app.demo.domain.user.User;
import app.demo.usecase.todo.QueryTasksUseCase;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(QueryTaskServices.class)
public class QueryTaskServicesTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private QueryTasksUseCase useCase;
    private final TaskToDo task1 = TaskToDo.builder().id("1").name("Task 1").description("Task Desc").assignedUserId("56").build();
    private final TaskToDo task2 = TaskToDo.builder().id("2").name("Task 2").description("Task Desc").assignedUserId("56").build();
    private final TaskToDo task3 = TaskToDo.builder().id("3").name("Task 3").description("Task Desc").build();
    private final User user = User.builder().name("Daniel").id("03").lastName("Ospina").build();


    @Before
    public void init() {
        final Flux<TaskToDo> tasks = Flux.just(
            task1,
            task2,
            task3
        );
        when(useCase.findAll()).thenReturn(tasks);

        Tuple2<TaskToDo, User> withDetails = Tuples.of(task1, user);
        when(useCase.findTodoWithDetails("02")).thenReturn(Mono.just(withDetails));
    }

    @Test
    public void shouldListAllTasks() {
        final WebTestClient.ResponseSpec spec = testClient.get().uri("/task")
            .exchange();
        spec.expectBodyList(TaskToDoDTO.class).consumeWith(res -> {
            final HttpStatus status = res.getStatus();
            final List<TaskToDoDTO> body = res.getResponseBody();
            assertThat(status.is2xxSuccessful()).isTrue();
            assertThat(body).hasSize(3).extracting(TaskToDoDTO::getName)
                .containsExactly("Task 1", "Task 2", "Task 3");
        });

    }

    @Test
    public void shouldFindWithDetails() {
        final WebTestClient.ResponseSpec spec = testClient.get().uri("/task/02")
            .exchange();

        spec.expectBody(TaskWithUser.class).consumeWith(res -> {
            final HttpStatus status = res.getStatus();
            final TaskWithUser body = res.getResponseBody();
            assertThat(status.is2xxSuccessful()).isTrue();
            assertThat(body.getTask().getName()).isEqualTo("Task 1");
            assertThat(body.getUser().getName()).isEqualTo("Daniel");
        });
    }

    @Data
    @NoArgsConstructor
    private static class TaskToDoDTO {
        private String id;
        private String name;
        private String description;

    }

    @Data
    @NoArgsConstructor
    private static class UserDTO {
        private String id;
        private String name;
        private String lastName;
    }

    @Data
    @NoArgsConstructor
    private static class TaskWithUser {
        private TaskToDoDTO task;
        private UserDTO user;
    }

}