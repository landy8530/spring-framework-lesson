package org.landy.springwebflux.web;

import org.landy.springwebflux.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;

import java.util.Optional;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;
/**
 * Created by Landy on 2019/1/16.
 * Web Flux 配置类型
 */
@Configuration
public class WebFluxConfiguration {

    //通过 Spring Boot Actuator /beans 来查看 "helloWorldRouterFunction"
    @Bean
    public RouterFunction helloWorldRouterFunction() {
        return route(GET("/webflux"),
                request -> {
                    Optional<String> message = request.queryParam("message");

                    return ok().body(just(message.get()), String.class);
                });
    }

    @Bean
    public RouterFunction userRouterFunction() {
        return route(POST("/webflux/user"),
                request -> {
//                    // JSON 格式表达 User
//                    Mono<User> userMono = request.bodyToMono(User.class);
//                    // User Java toString() 表达响应对象
//                    Mono<String> stringMono = userMono.map(user -> user.toString());

                    return ok().body(
                            request.bodyToMono(User.class)
//                                     .subscribeOn(Schedulers.parallel())
                                    .map(User::toString),
                            String.class);
                });
    }

}
