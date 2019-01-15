package org.landy.springxmlextension;

import org.landy.springxmlextension.domain.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class SpringDemo {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("context.xml");

        User user = context.getBean("user1",User.class);
        System.out.println(user);

        user = context.getBean("user2",User.class);
        System.out.println(user);
    }
}
