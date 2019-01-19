package org.landy.springtestdemo.service;


import org.landy.springtestdemo.domain.User;

import java.util.List;

/**
 * 用户的远程服务
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/18
 */
public interface UserRemoteService {

    List<User> findAll();
}
