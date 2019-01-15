package org.spring.webmvc.auto.annotation;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.List;

/**
 * 当制定的某个类存在时，满足条件
 * Created by Landy on 2019/1/8.
 */
public class OnClassConditional implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        boolean matched = false;

        //获取ConditionalOnClass中的所有属性方法
        MultiValueMap<String,Object> attributes = metadata.getAllAnnotationAttributes(ConditionalOnClass.class.getName());

        //获取value方法中的值
        List<Object> classes = attributes.get("value");

        ClassLoader classLoader = context.getClassLoader();

        try {
            for(Object clazz : classes) {
//                if(clazz instanceof Class) {
//                    return true;
//                }
                Class<?>[] type = (Class<?>[])clazz; //如果发生异常就说明转换不了,Class不存在
                matched = true;
            }
        } catch (Exception e) {
            matched = false;
        }


        System.out.println("OnClassConditional 是否匹配：" + matched);

        return matched;
    }
}
