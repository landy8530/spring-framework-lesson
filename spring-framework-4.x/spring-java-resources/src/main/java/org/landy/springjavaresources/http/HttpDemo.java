package org.landy.springjavaresources.http;

import org.springframework.http.HttpMethod;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by Landy on 2019/1/10.
 */
public class HttpDemo {

    public static void main(String[] args) throws Exception {
        //https://start.spring.io/
        // Spring RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        InputStream inputStreamFromRestTemplate =
                restTemplate.execute("https://start.spring.io/",
                        HttpMethod.GET,
                        request -> {
                        },
                        response -> {
                            return response.getBody();
                        }
                );

        System.out.println(inputStreamFromRestTemplate);

        URL url = new URL("https://start.spring.io/");

        InputStream inputStreamFromURL = url.openStream();

        System.out.println(inputStreamFromURL); //inputStreamFromRestTemplate对应的实现类与inputStreamFromURL对应的实现类相同

        String content = StreamUtils.copyToString(inputStreamFromURL, Charset.forName("UTF-8"));

        System.out.println(content);

    }

}
