
package app.demo.reactive.scheduler;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import app.demo.usecase.todo.CreateTasksUseCase;

@Component
@RequiredArgsConstructor // Injección de dependencias por constructor
public class SampleScheduler {

    private final CreateTasksUseCase createTasksUseCase;

    @Scheduled(cron = "0 0 17 * * *")
    // El scheduler crea una tarea diaramente (Ejemplo de negocio)
    public void checkExpiredMicrochips() {
        createTasksUseCase.createNew("Push Diario (Integrar continuamente)",
                "Se debe integrar el código " + "a la rama de integración diaramente.").subscribe(); // Se debe realizar
                                                                                                     // explicatamente
                                                                                                     // la subscripción
    }
}
