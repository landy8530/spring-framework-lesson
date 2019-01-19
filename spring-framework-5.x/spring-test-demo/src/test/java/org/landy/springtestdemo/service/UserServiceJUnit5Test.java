package org.landy.springtestdemo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.landy.springtestdemo.domain.User;
import org.landy.springtestdemo.service.impl.InMemoryUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * {@link UserService} JUnit 5 测试
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/18
 */
//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = InMemoryUserService.class)
@SpringJUnitConfig(classes = InMemoryUserService.class)
public class UserServiceJUnit5Test {

    @Autowired
    private UserService userService;

    /**
     * 测试 {@link UserService#save(User)} 方法
     */
    @Test
    public void testSave() {
        User user = new User();
        user.setId(1L);
        user.setName("Landy");
        // 第一次存，返回true
        assertTrue(userService.save(user));
        // 第二次存相同的内容，返回false
        assertFalse(userService.save(user));
    }

}
