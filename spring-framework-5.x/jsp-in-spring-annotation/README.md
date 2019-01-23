# JSP在Spring中的应用

## 1. JSP介绍

### 1.1 JSP EL

Expression Language

- c标签
- JSTL表达式：Java Standard Taglib Language

## 2. 前端控制器模式（Front Controller）

关于前端控制器请参考：http://www.corej2eepatterns.com/FrontController.htm

### 2.1 JSP Front Controller

### 2.2 Servlet Front Controller

## 3. Spring应用

### 3.1 Spring注解与xml装配的比较

Spring3.x的时候就已经支持Annotation驱动开发了，只是很多项目都是历史项目，已经是用XML配置了。

IOC是一种思想，原则，对依赖的管理，不需要显示的通过setter/getter进行管理，通过外部的配置进行相应的管理。

DI是IOC的一种实现方式，是它的一个落地，是一种技术，利用XML/Annotation的方式进行管理。

XML和Annotation没有好与坏，只不过IOC的作用被夸大了而已。

### 3.2 Spring Annotation应用（手动装配）

Annotation装配：利用注解的方式，就是不需要web.xml,app-context.xml等配置文件了，全部利用注解实现。

#### 3.2.1 DispatcherServlet

```java
/**
 * Web 自动装配
 */
//@EnableWebMvc
//@ComponentScan("org.landy.jsp.in.spring.web.controller")
//@Configuration
public class WebAutoConfiguration extends
        AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[]{characterEncodingFilter()};
    }

    private Filter characterEncodingFilter() {
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceRequestEncoding(true);
        filter.setForceResponseEncoding(true);
        return filter;
    }

    /**
     * 等价于 {@link ContextLoaderListener}
     * 即 root WebAPplicationContext，对应配置文件：WEB-INF/app-context.xml
     * @return
     */
    protected Class<?>[] getRootConfigClasses() {
        return new Class[0];
    }

    /***
     * 相当于 DispatcherServlet 加载 WEB-INF/jsp-in-spring.xml 文件
     * @return
     */
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{SpringWebMvcConfiguration.class};
    }

    /**
     * <servlet-mapping>
     * <servlet-name>app</servlet-name>
     * <url-pattern>/</url-pattern>
     * </servlet-mapping>
     *
     * @return
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }

    @Override
    protected String getServletName() {
        return "app";
    }
}
```



#### 3.2.2 InternalResourceViewResolver

```java
@EnableWebMvc
@ComponentScan("org.landy.jsp.in.spring.web.controller")
@Configuration
public class SpringWebMvcConfiguration {

    /**
     * <bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
     * <property name="viewClass" value="org.springframework.web.servlet.view.JstlView" />
     * <property name="prefix" value="/WEB-INF/jsp/"/>
     * <property name="suffix" value=".jsp"/>
     * </bean>
     */
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver
                = new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

}
```



#### 3.2.3 @Controller

就是扫描了相关的Controller注解

```java
@ComponentScan("org.landy.jsp.in.spring.web.controller")
```



#### 3.2.4 运行演示

- 打包

```
mvn clean package
```

- Jar包运行的方式

```
java -jar jsp-in-spring-annotation-1.0-SNAPSHOT-war-exec.jar
```

### 3.3 Spring XML应用（手动装配）

配置web.xml

#### 3.3.1 DispatcherServlet

```xml
<servlet>
    <servlet-name>app</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
    <init-param>
        <param-name>namespace</param-name>
        <!--配置的是WebApplicationContext配置文件的命名空间-->
        <!--对应的是app-servlet.xml文件的相对路径-->
        <!--如果为空，则默认为servletName + '-servlet',本例应是：app-servlet-->
        <!--具体路径拼接方法：/WEB-INF/ + {namespace} + .xml -->
        <!--然后把这个文件当作WebApplicationContext的上下文文件进行初始化工作-->
        <param-value>jsp-in-spring</param-value>
    </init-param>
</servlet>
```



#### 3.3.2 InternalResourceViewResolver

配置上面命名空间对应的jsp-in-spring.xml文件

```xml
<!--配置ViewResolver-->
<bean id="viewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
    <!--通过Java Bean的内省机制，就可以通过ClassEditor类可以把以下字符串转化为JstlView的Class对象-->
    <property name="viewClass" value="org.springframework.web.servlet.view.JstlView" />
    <property name="prefix" value="/WEB-INF/jsp/"/>
    <property name="suffix" value=".jsp"/>
</bean>
```



#### 3.3.3 @Controller

同样是配置上面命名空间对应的jsp-in-spring.xml文件

```xml
<!--使用注解配置-->
    <context:annotation-config />
    <!--需要扫描的注解包路径-->
    <context:component-scan base-package="org.landy.jsp.in.spring.web.controller" />
    <!--处理Annotation适配器-->
    <bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter" />
```



#### 3.3.4 运行演示

- 打包

```
mvn clean package
```

- Jar包运行的方式

```
java -jar jsp-in-spring-xml-1.0-SNAPSHOT-war-exec.jar
```

### 