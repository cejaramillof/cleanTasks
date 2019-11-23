package app.demo.reactive.example;

import lombok.Builder;
import lombok.Data;
import org.reactivecommons.api.domain.DomainEvent;
import org.reactivecommons.api.domain.DomainEventBus;
import org.reactivecommons.async.api.HandlerRegistry;
import org.reactivecommons.async.impl.config.annotations.EnableDomainEventBus;
import org.reactivecommons.async.impl.config.annotations.EnableMessageListeners;
import org.reactivestreams.Publisher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.reactivecommons.async.api.HandlerRegistry.register;
import static reactor.core.publisher.Mono.from;
import static reactor.core.publisher.Mono.just;

@SpringBootApplication
@EnableMessageListeners
@EnableDomainEventBus
public class ExternalAppServicesSimulator {

    static final String FIND_USERS_QUERY = "Users.user.findById";
    static final String ADD_POINTS_COMMAND = "Users.user.addPoints";

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ExternalAppServicesSimulator.class, args);
        TimeUnit.SECONDS.sleep(2);
        System.out.println("Wait for init Ok");
    }

    @Bean
    public HandlerRegistry registry() {
        return register()
            .handleCommand(ADD_POINTS_COMMAND, cmd -> just(cmd).log().then(), AddPoints.class)
            .serveQuery(FIND_USERS_QUERY, userId -> just(User.builder().id(userId).name("Daniel").lastName("Ospina").build()), String.class);
    }

    static final String USER_REMOVED_EVENT = "users.user.removedFromProject";

    @Bean
    public CommandLineRunner lineRunner(DomainEventBus bus) {
        return args -> {
            final Publisher<Void> emit = bus.emit(new DomainEvent<>(USER_REMOVED_EVENT, UUID.randomUUID().toString(), "35"));
            from(emit).delaySubscription(Duration.ofMillis(2500)).subscribe();
        };
    }

    @Data
    static class AddPoints {
        private String userId;
        private Integer points;
    }

    @Builder
    @Data
    static class User {
        private String id;
        private String name;
        private String lastName;
    }

}
