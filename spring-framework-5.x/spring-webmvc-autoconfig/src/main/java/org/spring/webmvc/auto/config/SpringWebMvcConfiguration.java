package org.spring.webmvc.auto.config;

import org.spring.webmvc.auto.annotation.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring MVC 配置BEAN
 * Created by Landy on 2019/1/7.
 */
@Configuration
@ComponentScan(basePackages = "org.spring.webmvc.auto")
public class SpringWebMvcConfiguration {

    @ConditionalOnClass(String.class) //当这个类不存在的时候，就不装配，比如有时候这个类是第三方Jar包的时候，有可能不存在
    @Bean("helloWorld") //
    public String helloWorld() {
        return "helloWorld";
    }

}
