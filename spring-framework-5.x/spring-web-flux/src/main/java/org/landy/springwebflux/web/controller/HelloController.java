package org.landy.springwebflux.web.controller;

import org.landy.springwebflux.domain.User;
import org.landy.springwebflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Optional;

/**
 * Created by Landy on 2019/1/16.
 */
@RestController
public class HelloController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
    public String handle() {
        return "Hello WebFlux";
    }

    @GetMapping
    public Optional<String> index(@RequestParam(required = false)
                                          String message) {
        return StringUtils.hasText(message) ? Optional.of(message) : Optional.empty();
    }

//    @GetMapping
//    public Optional<String> index() {
//        return Optional.of("Hello WebFlux");
//    }

    @GetMapping("/mono")
    public Mono<String> mono() {
        return Mono.just("Hello WebFlux");
    }

    @GetMapping("/param")
    public Mono<String> param(@RequestParam String message) {
        return Mono.just(message);
    }

    //http://localhost:8080/user/save test in postman
//    @PostMapping("/user/save")
//    public Mono<User> save(User user) {
//        return Mono.just(user);
//    }

    @PostMapping("/user/save")
    public Mono<User> saveUser(User user) {
        if (userRepository.saveUser(user)) {
            return Mono.just(user);
        }
        return Mono.empty();
    }

    @GetMapping("/user/list")
    public Collection<User> users() {
        return userRepository.findAll();
    }

    @GetMapping("/user/flux")
    public Flux<User> usersList() {
        Collection<User> users = userRepository.findAll();
        return Flux.fromIterable(users);
    }

}
