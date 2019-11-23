package app.demo.reactive;

import app.demo.usecase.todo.CompleteTasksUseCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivecommons.api.domain.Command;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.api.handlers.registered.RegisteredCommandHandler;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;
import app.demo.usecase.todo.AssignTasksUseCase;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandHandlersConfigTest {

    @InjectMocks
    private CommandHandlersConfig config;

    @Mock
    private CompleteTasksUseCase completeTasksUseCase;

    @Mock
    private AssignTasksUseCase assignTasksUseCase;
    private final String taskId = "45";
    private final String userId = "98";

    @Test
    public void shouldRegisterCommands() {
        final HandlerRegistry registry = config.registry();
        final List<RegisteredCommandHandler> commandHandlers = registry.getCommandHandlers();
        assertThat(commandHandlers).hasSize(2);
    }

    @Test
    public void shouldRegisterAssignTaskCommand() {
        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(assignTasksUseCase.assignTask(taskId, userId)).thenReturn(probe.mono());

        final HandlerRegistry registry = config.registry();
        final List<RegisteredCommandHandler> commandHandlers = registry.getCommandHandlers();
        final RegisteredCommandHandler handler = commandHandlers.stream().filter(h -> h.getPath().equals(CommandHandlersConfig.ASSIGN_TASK_COMMAND)).findFirst().get();
        CommandHandlersConfig.AssignTaskData data = new CommandHandlersConfig.AssignTaskData();
        data.setTaskId(taskId);
        data.setUserId(userId);
        final Mono result = handler.getHandler().handle(new Command<>(CommandHandlersConfig.ASSIGN_TASK_COMMAND, "23", data));

        StepVerifier.create(result).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }


    @Test
    public void shouldRegisterCompleteTaskCommand() {
        PublisherProbe<Void> probe = PublisherProbe.empty();
        when(completeTasksUseCase.markAsDone(taskId)).thenReturn(probe.mono());

        final HandlerRegistry registry = config.registry();
        final List<RegisteredCommandHandler> commandHandlers = registry.getCommandHandlers();
        final RegisteredCommandHandler handler = commandHandlers.stream().filter(h -> h.getPath().equals(CommandHandlersConfig.COMPLETE_TASK_COMMAND)).findFirst().get();

        final Mono result = handler.getHandler().handle(new Command<>(CommandHandlersConfig.COMPLETE_TASK_COMMAND, "23", taskId));

        StepVerifier.create(result).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasRequested();
        probe.assertWasNotCancelled();
    }
}