package org.landy.springwebflux.repository;

import org.landy.springwebflux.domain.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Landy on 2019/1/16.
 */
@Repository
public class UserRepository {

    private Map<Long, User> repository = new ConcurrentHashMap<Long, User>();

    public boolean saveUser(User user) {
        return repository.put(user.getId(), user) == null;
    }

    public Collection<User> findAll() {
        return repository.values();
    }

    public Flux<User> allUsers(){
        return Flux.fromIterable(findAll());
    }

}
