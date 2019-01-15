package org.landy.annotationdrivendevelopment.bootstrap;

import org.landy.annotationdrivendevelopment.domain.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * XML装配
 * Created by Landy on 2019/1/7.
 */
public class XmlConfigBootstrap {

    public static void main(String[] args) {
        // 构建一个 Spring Application 上下文
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext();

        //注册xml配置
        applicationContext.setConfigLocations(
                "classpath:/META-INF/spring/context.xml");
        //启动方法
        applicationContext.refresh();

        User user = applicationContext.getBean("user", User.class);

        System.out.printf("user.getName() = %s \n",user.getName());



    }

}
