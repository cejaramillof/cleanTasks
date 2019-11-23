package app.demo.web.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import app.demo.usecase.todo.AssignTasksUseCase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(AssignTasksService.class)
public class AssignTasksServiceTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private AssignTasksUseCase useCase;
    private final String userId = "35";
    private final String taskId = "56";

    @Before
    public void init() {
        when(useCase.assignTask(taskId, userId)).thenReturn(Mono.empty());
    }

    @Test
    public void assignTaskRestTest() {

        final WebTestClient.ResponseSpec spec = testClient.post().uri("/task/assign")
            .contentType(MediaType.APPLICATION_JSON)
            .syncBody("{\"taskId\": \"56\", \"userId\": \"35\"}")
            .exchange();

        spec.expectStatus().isOk();
        verify(useCase).assignTask(taskId, userId);

    }

}