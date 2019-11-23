package app.demo.web.task;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import app.demo.usecase.todo.CompleteTasksUseCase;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static reactor.core.publisher.Mono.empty;

@RunWith(SpringRunner.class)
@WebFluxTest(CompleteTasksService.class)
public class CompleteTasksServiceTest {

    @Autowired
    private WebTestClient testClient;

    @MockBean
    private CompleteTasksUseCase useCase;
    private final String taskId = "56";

    @Before
    public void init() {
        when(useCase.markAsDone(taskId)).thenReturn(empty());
    }

    @Test
    public void assignTaskRestTest() {
        final ResponseSpec spec = testClient.post()
            .uri("/task/"+taskId+"/complete")
            .exchange();

        spec.expectStatus().isOk();
        verify(useCase).markAsDone(taskId);
    }

}