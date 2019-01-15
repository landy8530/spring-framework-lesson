package org.landy.springjavaresources.file;

import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by Landy on 2019/1/10.
 */
public class FileDemo {

    public static void main(String[] args) throws IOException {
        File file1 = new File(""); //默认为用户路径
        System.out.println(file1.getAbsolutePath());
        System.out.println(System.getProperty("user.dir"));//用户当前工作路径

        File file = new File("D:\\projects\\idea_workspace\\spring-boot-lesson\\spring-java-resources\\src\\main\\resources\\application.properties");

        URL fileURL = file.toURI().toURL();
        System.out.println(fileURL);
        //统一资源定位符 URL
//        URL url = new URL("https://www.baidu.com"); // https 协议
//        URL ftpURL = new URL("ftp://ftp.baidu.com"); // ftp 协议
//        URL jar = new URL("jar://jar.baidu.com"); // jar 协议
//        URL dubboURL = new URL("dubbo://");       // dubbo协议
//        URL classpathURL = new URL("classpath:/");       // classpath
        //策略模式的实现
        // REMIND: decide whether to allow the "null" class prefix
        // or not.
        //packagePrefixList += "sun.net.www.protocol";
        // file URLStreamHandler   = sun.net.www.protocol.file.Handler
        // http URLStreamHandler  =  sun.net.www.protocol.http.Handler
        // https URLStreamHandler  = sun.net.www.protocol.https.Handler
        // jar URLStreamHandler  = sun.net.www.protocol.jar.Handler
        // ftp URLStreamHandler  = sun.net.www.protocol.ftp.Handler
        // 模式 URLStreamHandler =  sun.net.www.protocol.${protocol}.Handler

        URLConnection urlConnection = fileURL.openConnection();

        InputStream inputStreamFromURL = urlConnection.getInputStream();

        String content = StreamUtils.copyToString(inputStreamFromURL, Charset.forName("UTF-8"));

        System.out.println(content);

        // URL -> URLConnection -> URLStreamHandler -> InputStream


    }

}
