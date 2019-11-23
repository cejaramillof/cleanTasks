package app.demo.reactive.events;

import app.demo.domain.common.EventsGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import app.demo.domain.common.Event;

import java.util.UUID;
import java.util.logging.Level;

import static reactor.core.publisher.Mono.from;

@Log
@Component
@EnableDomainEventBus
@RequiredArgsConstructor
//Permite personalizar la emisión de eventos, enriquecerla o interceptarla.
// Por defecto delega el proceso en reactive-commons.
public class ReactiveEventsGateway implements EventsGateway {

    private final DomainEventBus domainEventBus;

    @Override
    public Mono<Void> emit(Event event) {
        log.log(Level.INFO, "Emitiendo evento de dominio: {0}: {1}", new String[]{event.name(), event.toString()});
        return from(domainEventBus.emit(new DomainEvent<>(event.name(), UUID.randomUUID().toString(), event)));
    }
}
