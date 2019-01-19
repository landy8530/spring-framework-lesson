package org.landy.springtestdemo.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.landy.springtestdemo.domain.User;
import org.landy.springtestdemo.service.impl.InMemoryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * {@link UserService} JUnit 4 测试
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/18
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = InMemoryUserService.class)
public class UserServiceJUnit4Test {

    @Autowired
    private UserService userService;

    /**
     * 测试 {@link UserService#save(User)} 方法
     */
    @Test
    public void testSave(){
        User user = new User();
        user.setId(1L);
        user.setName("小马哥");
        // 第一次存，返回true
        assertTrue(userService.save(user));
        // 第二次存相同的内容，返回false
        assertFalse(userService.save(user));
    }

}
