package org.landy.springjavabeans.spring;

import org.landy.springjavabeans.domain.User;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Landy on 2019/1/8.
 */
public class SpringPropertyEditorDemo {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext();
        context.setConfigLocation("context.xml");
        context.refresh();
        //依赖查找
        User user = context.getBean("user",User.class);

        System.out.println(user);

    }
}

