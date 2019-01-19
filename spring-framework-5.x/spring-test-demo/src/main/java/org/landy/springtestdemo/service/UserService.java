package org.landy.springtestdemo.service;


import org.landy.springtestdemo.domain.User;

import java.util.List;

/**
 * 用户服务
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/18
 */
public interface UserService {

    boolean save(User user);

    List<User> findAll();
}
