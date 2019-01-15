package org.landy.springjavaresources.spring.resource;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ProtocolResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StreamUtils;

import java.io.InputStream;
import java.nio.charset.Charset;

/**
 * Created by Landy on 2019/1/10.
 */
public class ResourceDemo {

    public static void main(String[] args) throws Exception {
        // Resource
        // FileSystemResource
        // ClasspathResource
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        // 添加一个protocol = "cp" 处理
        resourceLoader.addProtocolResolver(new  ProtocolResolver() {

            private static final String PROTOCOL_PREFIX = "cp:/";

            @Override
            public Resource resolve (String location, ResourceLoader resourceLoader){
                if (location.startsWith(PROTOCOL_PREFIX)) {
                    // application.properties
                    String classpath = ResourceLoader.CLASSPATH_URL_PREFIX +
                            location.substring(PROTOCOL_PREFIX.length());
                    // cp:/application.properties -> classpath:application.properties
                    return resourceLoader.getResource(classpath);
                }
                return null;
            }
        });

        Resource resource = resourceLoader.getResource("cp:/application.properties");
        //resource.getURL();
        InputStream inputStream = resource.getInputStream();

        String content = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));

        System.out.println(content);
    }


}
