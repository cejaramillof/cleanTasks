package app.demo.reactive.events;

import app.demo.domain.common.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.Mockito.*;

@SuppressWarnings("unchecked")
public class ReactiveEventsGatewayTest {


    private final DomainEventBus eventBus = mock(DomainEventBus.class);
    private final ReactiveEventsGateway gateway = new ReactiveEventsGateway(eventBus);

    private final ArgumentCaptor<DomainEvent<MyEvent>> captor = forClass(DomainEvent.class);


    @Test
    public void testEmit() {
        when(eventBus.emit(any())).thenReturn(Mono.empty());

        final Mono<Void> result = gateway.emit(new MyEvent("1", "2"));

        StepVerifier.create(result).verifyComplete();

        verify(eventBus).emit(captor.capture());
        final DomainEvent<MyEvent> event = captor.getValue();
        assertThat(event.getName()).isEqualTo("test.event");
        assertThat(event.getData()).isEqualTo(new MyEvent("1", "2"));
    }


    @Test
    public void shouldRespectBasicReactiveStreamContract() {
        PublisherProbe probe = PublisherProbe.empty();
        when(eventBus.emit(any())).thenReturn(probe.mono());

        final Mono<Void> result = gateway.emit(new MyEvent("1", "2"));

        StepVerifier.create(result).verifyComplete();
        probe.assertWasSubscribed();
        probe.assertWasNotCancelled();
        probe.assertWasRequested();
    }

    @Data
    @AllArgsConstructor
    private static class MyEvent implements Event {

        private String data1;
        private String data2;

        @Override
        public String name() {
            return "test.event";
        }
    }

}