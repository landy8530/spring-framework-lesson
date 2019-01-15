package org.landy.springjavaresources.file;

import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Landy on 2019/1/10.
 */
public class PropertiesDemo {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        //1. 绝对路径方式，不妥
//        File file = new File("D:\\projects\\idea_workspace\\spring-boot-lesson\\spring-java-resources\\src\\main\\resources\\application.properties");
//        properties.load(new FileReader(file));
        //2. 可以读取class path路径下的,太多，不可取
//        System.out.println(System.getProperty("java.class.path"));

        //3. 利用ClassLoader，读取class path路径下的资源（相对路径）
        //Maven默认的Resource目录就会放到class path下
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //读取class path路径下的文件
        InputStream is = classLoader.getResourceAsStream("application.properties");
        properties.load(is);
        System.out.println(properties.getProperty("spring.application.name"));
    }

}
