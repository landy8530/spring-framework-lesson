package org.landy.annotationdrivendevelopment.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Landy on 2019/1/7.
 */
@RestController
public class DemoController {

    @GetMapping("/helloWorld")
    public String helloWorld(){
        return "Hello, World!";
    }

}