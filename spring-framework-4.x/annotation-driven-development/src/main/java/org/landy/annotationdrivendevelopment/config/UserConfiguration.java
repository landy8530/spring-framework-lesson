package org.landy.annotationdrivendevelopment.config;

import org.landy.annotationdrivendevelopment.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * User 配置BEAN
 * Created by Landy on 2019/1/7.
 */
@Configuration
public class UserConfiguration {

    /**
     *
     * @return
     */
    @Bean(name = "user")
    public User user(){
        User user = new User();
        user.setName("Landy V5");
        return user;
    }
}
