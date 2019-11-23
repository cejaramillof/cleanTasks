package app.demo.reactive.rest;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import app.demo.domain.user.User;
import app.demo.domain.user.gateway.UserGateway;

@AllArgsConstructor
@Component
public class UsersRestAdapter implements UserGateway {

    private final WebClient webClient;

    @Override
    public Mono<User> findById(String id) {
        return webClient.get().uri("/user/{0}", id).exchange().flatMap(clientResponse -> clientResponse.bodyToMono(User.UserBuilder.class))
                .map(x -> x.build());
    }
}
