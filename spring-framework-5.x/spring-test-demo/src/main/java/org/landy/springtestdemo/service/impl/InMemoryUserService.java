package org.landy.springtestdemo.service.impl;

import org.landy.springtestdemo.domain.User;
import org.landy.springtestdemo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存实现 {@link UserService}
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/18
 */
@Service
public class InMemoryUserService implements UserService {

    private Map<Long, User> repository = new ConcurrentHashMap<>();

    @Override
    public boolean save(User user) {
        return repository.put(user.getId(), user) == null;
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(repository.values());
    }
}
