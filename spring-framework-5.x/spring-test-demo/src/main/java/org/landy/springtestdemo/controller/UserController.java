package org.landy.springtestdemo.controller;

import org.landy.springtestdemo.domain.User;
import org.landy.springtestdemo.service.UserRemoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  User Controller
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/18
 */
@RestController
public class UserController {

    @Autowired
    private UserRemoteService userRemoteService;

    public List<User> findAll() {
        return userRemoteService.findAll();
    }

}
