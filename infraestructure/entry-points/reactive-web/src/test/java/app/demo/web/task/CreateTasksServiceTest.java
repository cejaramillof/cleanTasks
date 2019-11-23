package app.demo.web.task;

import app.demo.domain.todo.TaskToDo;
import lombok.Data;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import app.demo.usecase.todo.CreateTasksUseCase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static reactor.core.publisher.Mono.just;

@RunWith(SpringRunner.class)
@WebFluxTest(CreateTasksService.class)
public class CreateTasksServiceTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private CreateTasksUseCase useCase;
    private final String name = "Task 1";
    private final String description = "Desc Test task";

    @Before
    public void init() {
        when(useCase.createNew(name, description)).then(i ->
            just(TaskToDo.builder().name(name).description(description).id("01").build()));
    }

    @Test
    public void createNewTaskRestTest() {

        final WebTestClient.ResponseSpec spec = testClient.post().uri("/task")
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody("{\"name\": \"Task 1\", \"description\": \"Desc Test task\"}")
            .exchange();

        spec.expectBody(TaskToDoDTO.class).consumeWith(res -> {
            final HttpStatus status = res.getStatus();
            final TaskToDoDTO body = res.getResponseBody();
            assertThat(status.is2xxSuccessful()).isTrue();
            assertThat(body).extracting(TaskToDoDTO::getId, TaskToDoDTO::getName, TaskToDoDTO::getDescription)
                .containsExactly("01", name, description);
        });

    }

    @Data
    private static class TaskToDoDTO {
        private String id;
        private String name;
        private String description;

    }

}