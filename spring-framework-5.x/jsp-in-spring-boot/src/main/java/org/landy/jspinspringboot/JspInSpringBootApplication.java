package org.landy.jspinspringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.landy.jspinspringboot.web.controller")
public class JspInSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(JspInSpringBootApplication.class, args);
	}
}
