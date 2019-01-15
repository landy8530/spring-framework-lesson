package org.landy.springjavabeans;

import org.landy.springjavabeans.domain.User;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * Created by Landy on 2019/1/8.
 */
public class JavaBeansDemo {

    public static void main(String[] args) throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(User.class,Object.class);
        //Bean描述符（BeanDescriptor）
        BeanDescriptor beanDescriptor = beanInfo.getBeanDescriptor();
        System.out.println(beanDescriptor);
        //- 方法描述符（MethodDescriptor）
        MethodDescriptor[] methodDescriptors = beanInfo.getMethodDescriptors();
//        Stream.of(methodDescriptors).forEach((methodDescriptor -> {
//            System.out.println(methodDescriptor);
//        }));
        Stream.of(methodDescriptors).forEach(System.out::println);

        //- 字段描述符（FieldDecriptor）
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//        Stream.of(propertyDescriptors).forEach(System.out::println);

        // 创建一个 User Bean
        User user = new User();

        Stream.of(propertyDescriptors).forEach(propertyDescriptor -> {

            String propertyName = propertyDescriptor.getName(); // id 或者 name
            if ("id".equals(propertyName)) { // 属性名称等于 "id" , 类型 long
                propertyDescriptor.setPropertyEditorClass(IdPropertyEditor.class);
                PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(user);
//                propertyEditor.addPropertyChangeListener((event)->{
//                    Object newValue = event.getNewValue(); //newValue会为空，可能是JDK的bug，故采用以下的事件源对象获取
//                    Method setterMethod = propertyDescriptor.getWriteMethod();
//                    try {
//                        setterMethod.invoke(user, newValue);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                });
                propertyEditor.addPropertyChangeListener(
                        new SetPropertyChangeListener(user,propertyDescriptor.getWriteMethod()));
                // 触发事件，同时事件源 propertyEditor
                propertyEditor.setAsText("99");
            } else if ("date".equals(propertyName)) { // date Date

                propertyDescriptor.setPropertyEditorClass(DatePropertyEditor.class);
                PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(user);
                propertyEditor.addPropertyChangeListener(
                        new SetPropertyChangeListener(user,propertyDescriptor.getWriteMethod()));
                propertyEditor.setAsText("2017-11-25");
            }
        });

        System.out.println(user); // 输出 user
    }

    private static class SetPropertyChangeListener implements PropertyChangeListener {

        private final Object bean;
        private final Method setterMethod;

        private SetPropertyChangeListener(Object bean, Method setterMethod) {
            this.setterMethod = setterMethod;
            this.bean = bean;
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            PropertyEditor source = (PropertyEditor) event.getSource();
            try {
                setterMethod.invoke(bean, source.getValue());
            } catch (Exception e) {
            }
        }
    }

}

