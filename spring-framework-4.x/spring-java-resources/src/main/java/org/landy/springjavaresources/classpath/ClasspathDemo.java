package org.landy.springjavaresources.classpath;

import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

/**
 * Created by Landy on 2019/1/10.
 */
public class ClasspathDemo {

    public static void main(String[] args) throws Exception {
        // Spring Classpath protocol
        // classpath:/META-INF/license.txt
//         URL url = classLoader.getResource("META-INF/license.txt");
//        URL url = new URL("classpath:/META-INF/license.txt");
        URL url = new URL("classpath:/application.properties");

        URLConnection urlConnection = url.openConnection();

        InputStream inputStreamFromURL = urlConnection.getInputStream();

        String content = StreamUtils.copyToString(inputStreamFromURL, Charset.forName("UTF-8"));

        System.out.println(content); //unknown protocol: classpath

    }

}
