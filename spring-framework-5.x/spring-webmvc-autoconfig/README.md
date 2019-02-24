# Spring Web自动装配（Annotation）

## 1. Annotation装配

### 替代XML装配

### 优势

### 不足

## 2. Web自动化装配

### 2.1 原理

- Servlet3.0 + 自动装配

- 利用编程的手段添加和配置Filter组件

  - ServletRegistrationBean -> ServletContext

- Springboot: DispatcherServletAutoConfiguration自动装配

- 生命周期

  - ServletContainerInitializer#onStartup 当容器启动时

  ```java
  javax.servlet.ServletContainerInitializer
  ```

  - ServletContextListener#contextInitialized 当ServletContext初始化时

  ```java
  javax.servlet.ServletContextListener
  ```

### 2.2 Spring Web自动装配

- Spring Web实现

  ```java
  @HandlesTypes(WebApplicationInitializer.class)//选择关心的类以及派生类，本例选择WebApplicationInitializer的子类
  public class SpringServletContainerInitializer implements ServletContainerInitializer {
      //webAppInitializerClasses 关心的类对象，需要初始化的
      @Override
  	public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
  			throws ServletException {
          List<WebApplicationInitializer> initializers = new LinkedList<WebApplicationInitializer>();
  
  		if (webAppInitializerClasses != null) {
  			for (Class<?> waiClass : webAppInitializerClasses) {
  				// Be defensive: Some servlet containers provide us with invalid classes,
  				// no matter what @HandlesTypes says...
  				if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
  						WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
  					try {
  						initializers.add((WebApplicationInitializer) waiClass.newInstance());
  					}
  					catch (Throwable ex) {
  						throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
  					}
  				}
  			}
  		}
          ....
      }
  }    
  ```

  默认地扫描WEB/classes和WEB/lib下面的类和Jar包

  以Spring Web为例，它关注的是WebApplicationInitializer的子类，比如：

  ```java
  org.springframework.web.context.AbstractContextLoaderInitializer
  ```

  根据它的类名规则（Abstract ContextLoader Initializer），它会注册一个ContextLoaderListener

  ```java
  org.springframework.web.context.ContextLoaderListener
  ```

  再比如WebApplicationInitializer的其他实现类：

  ```
  org.springframework.web.servlet.support.AbstractDispatcherServletInitializer
  ```

  根据它的类名规则（Abstract DispatcherServlet Initializer），它就会注册一个DispatcherServlet，

  ```
  org.springframework.web.servlet.DispatcherServlet
  ```

  再继续看AbstractDispatcherServletInitializer的子类AbstractAnnotationConfigDispatcherServletInitializer

  ```java
  org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer
  ```

  根据它的类名规则（Abstract Annotation Config DispatcherServlet Initializer），他会注册一个DispatcherServlet，并且是根据注解自动配置的。

- 由此可见，他们的类层次关系如下：

```
WebApplicationInitializer
	|-AbstractContextLoaderInitializer
		|-AbstractDispatcherServletInitializer
			|-AbstractAnnotationConfigDispatcherServletInitializer
```

- 

### 2.3 实现Spring Web MVC自动装配

#### 2.3.1 Tomcat实现规范

- Tomcat 6.x 实现Servlet 2.5 规范
- Tomcat 7.x 实现Servlet 3.0 规范
- Tomcat 8.x 实现Servlet 3.1 规范
- Tomcat 9.x 实现Servlet 4 规范

#### 2.3.2 打包与执行

- 打包

```
mvn clean package
```

- 执行

```
java -jar spring-webmvc-autoconfig-1.0-SNAPSHOT-war-exec.jar
```

#### 2.3.3 实现功能

实现了Spring Boot的两个功能（本案例其实并没有利用Spring Boot的自动化配置（Web.xml并没有配置SpringBoot相关））：

- 实现了自动装配（利用Annotation）
- 实现了利用jar启动应用程序

- 代码：https://github.com/landy8530/spring-framework-lesson/tree/master/spring-framework-5.x/spring-webmvc-autoconfig

#### 2.3.4 调试自动装配过程

- 调试命令

```
java -jar -agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=9527 spring-webmvc-autoconfig-1.0-SNAPSHOT-war-exec.jar
```

suspend=y,表示需要阻塞

经过调试，发现只有非抽象类或者非接口才能进行自动装配操作。

#### 2.3.5 插件说明

嵌入式Tomcat/Jetty插件，它会有自己的API，比如：https://tomcat.apache.org/download-70.cgi

## 3. 条件化装配

### 3.1 Spring条件装配

当某个类不存在的时候，就不装配，比如有时候这个类是第三方Jar包的时候，有可能不存在

### 3.2 @Conditional

Spring Context中的一个条件装配注解：org.springframework.context.annotation.Conditional

```java
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Conditional {

	/**
	 * All {@link Condition}s that must {@linkplain Condition#matches match}
	 * in order for the component to be registered.
	 */
	Class<? extends Condition>[] value();

}
```

### 3.3 实现Spring Boot @ConditionOnClass

#### 3.3.1 Spring Boot @ConditionOnClass

##### 3.3.1.1 注解定义

```java
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnClassCondition.class)
public @interface ConditionalOnClass {

	/**
	 * The classes that must be present. Since this annotation parsed by loading class
	 * bytecode it is safe to specify classes here that may ultimately not be on the
	 * classpath.
	 * @return the classes that must be present
	 */
	Class<?>[] value() default {};

	/**
	 * The classes names that must be present.
	 * @return the class names that must be present.
	 */
	String[] name() default {};

}
```

##### 3.3.1.2 注解实现

```java
/**
 * {@link Condition} that checks for the presence or absence of specific classes.
 *
 * @author Phillip Webb
 * @see ConditionalOnClass
 * @see ConditionalOnMissingClass
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
class OnClassCondition extends SpringBootCondition {

	@Override
	public ConditionOutcome getMatchOutcome(ConditionContext context,
			AnnotatedTypeMetadata metadata) {
		ConditionMessage matchMessage = ConditionMessage.empty();
		MultiValueMap<String, Object> onClasses = getAttributes(metadata,
				ConditionalOnClass.class);
		if (onClasses != null) {
			List<String> missing = getMatchingClasses(onClasses, MatchType.MISSING,
					context);
			if (!missing.isEmpty()) {
				return ConditionOutcome
						.noMatch(ConditionMessage.forCondition(ConditionalOnClass.class)
								.didNotFind("required class", "required classes")
								.items(Style.QUOTE, missing));
			}
			matchMessage = matchMessage.andCondition(ConditionalOnClass.class)
					.found("required class", "required classes").items(Style.QUOTE,
							getMatchingClasses(onClasses, MatchType.PRESENT, context));
		}
		MultiValueMap<String, Object> onMissingClasses = getAttributes(metadata,
				ConditionalOnMissingClass.class);
		if (onMissingClasses != null) {
			List<String> present = getMatchingClasses(onMissingClasses, MatchType.PRESENT,
					context);
			if (!present.isEmpty()) {
				return ConditionOutcome.noMatch(
						ConditionMessage.forCondition(ConditionalOnMissingClass.class)
								.found("unwanted class", "unwanted classes")
								.items(Style.QUOTE, present));
			}
			matchMessage = matchMessage.andCondition(ConditionalOnMissingClass.class)
					.didNotFind("unwanted class", "unwanted classes")
					.items(Style.QUOTE, getMatchingClasses(onMissingClasses,
							MatchType.MISSING, context));
		}
		return ConditionOutcome.match(matchMessage);
	}

	....

}
```

#### 3.3.2 自定义实现Spring Boot @ConditionOnClass

##### 3.3.2.1 注解定义

```java
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Conditional(OnClassConditional.class)
public @interface ConditionalOnClass {

    Class<?>[] value();

}
```

##### 3.3.2.2 注解实现

```java
**
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
            }
        } catch (Exception e) {
            matched = false;
        }


        System.out.println("OnClassConditional 是否匹配：" + matched);

        return matched;
    }
}
```

