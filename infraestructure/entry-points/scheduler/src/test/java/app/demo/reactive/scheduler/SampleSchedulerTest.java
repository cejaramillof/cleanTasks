package app.demo.reactive.scheduler;

import app.demo.domain.todo.TaskToDo;
import app.demo.usecase.todo.CreateTasksUseCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.test.publisher.PublisherProbe;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SampleSchedulerTest {

    @InjectMocks
    private SampleScheduler scheduler;

    @Mock
    private CreateTasksUseCase useCase;

    @Test
    public void checkExpiredMicrochips() {
        PublisherProbe<TaskToDo> probe = PublisherProbe.of(Mono.just(TaskToDo.builder().build()));
        when(useCase.createNew(any(), any())).thenReturn(probe.mono());

        scheduler.checkExpiredMicrochips();

        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }
}