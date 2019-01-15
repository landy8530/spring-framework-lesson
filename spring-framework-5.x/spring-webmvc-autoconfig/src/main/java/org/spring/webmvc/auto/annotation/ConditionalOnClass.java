package org.spring.webmvc.auto.annotation;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * Created by Landy on 2019/1/8.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnClassConditional.class)
public @interface ConditionalOnClass {

    Class<?>[] value();

}
