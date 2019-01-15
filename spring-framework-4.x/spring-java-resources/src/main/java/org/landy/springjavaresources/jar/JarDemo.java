package org.landy.springjavaresources.jar;

import org.springframework.context.ApplicationContext;

import java.net.URL;

/**
 * Created by Landy on 2019/1/10.
 */
public class JarDemo {

    public static void main(String[] args) {
        ClassLoader classLoader = ApplicationContext.class.getClassLoader();
        //读取Jar包中的资源，其协议为jar协议
        URL url = classLoader.getResource("META-INF/license.txt");
        //jar:file:/D:/mvn_repository/repository/org/springframework/spring-web/4.3.4.RELEASE/spring-web-4.3.4.RELEASE.jar!/META-INF/license.txt
        System.out.println(url);
    }


}
