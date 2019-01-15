package org.spring.webmvc.auto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Landy on 2019/1/7.
 */
@RestController
public class DemoController {

    @Autowired(required = false)
    @Qualifier("helloWorld") //此处的别名是Bean Name
    private String helloWorld;

    @GetMapping
    public String index() {
        return helloWorld;
        //return "Spring MVC auto configuration";
    }

}
