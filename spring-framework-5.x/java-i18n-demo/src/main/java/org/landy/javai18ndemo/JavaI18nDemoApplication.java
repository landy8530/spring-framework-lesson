package org.landy.javai18ndemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import java.util.ResourceBundle;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.gupao.javai18ndemo.javaee")
public class JavaI18nDemoApplication {

	public static void main(String[] args) {
		//使用默认的DefaultControl（即实现了ResourceBundleControlProvider的类）
		//只能把配置文件加载到当前的JVM中才能被加载相应的Provider，加载到当前Application Class Path是会被忽略的。
//		ResourceBundle resourceBundle4SPI = ResourceBundle.getBundle("static.default");
//		System.out.println("resourceBundle4SPI.name : " + resourceBundle4SPI.getString("name"));

		SpringApplication.run(JavaI18nDemoApplication.class, args);
	}
}
