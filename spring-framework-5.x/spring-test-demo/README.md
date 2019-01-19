# Spring5新特性之测试

## JUnit5

- Junit Platform：启动测试框架的基础

- Junit Jupiter：测试框架的编程模型和扩展模型

- Junit Vintage：JUnit3、JUnit4的测试引擎

### JUnit Jupiter

#### JUnit 4 与 JUnit 5 中的注解比较

| JUnit 5     | JUnit 4      | 说明                                                         |
| ----------- | ------------ | ------------------------------------------------------------ |
| @Test       | @Test        | 被注解的方法是一个测试方法。与 JUnit 4 相同。                |
| @BeforeAll  | @BeforeClass | 被注解的（静态）方法将在当前类中的所有 @Test 方法前执行一次。 |
| @BeforeEach | @Before      | 被注解的方法将在当前类中的每个 @Test 方法前执行。            |
| @AfterEach  | @After       | 被注解的方法将在当前类中的每个 @Test 方法后执行。            |
| @AfterAll   | @AfterClass  | 被注解的（静态）方法将在当前类中的所有 @Test 方法后执行一次。 |
| @Disabled   | @Ignore      | 被注解的方法不会执行（将被跳过），但会报告为已执行。         |

## Spring5 测试

### Spring5 单元测试

#### Mock对象

- Environment
- Servlet API
- JNDI

### Spring5 集成测试

#### Spring TestContext Framework

- TestContext  测试上下文
  - `org.springframework.test.context.TestContext` 
  - 测试上下文的整合，包括Spring上下文和JUnit上下文

- @ContextConfiguration 
  - `org.springframework.test.context.ContextConfiguration`
  - 由于传统的Junit4不兼容RunWith（`org.junit.runner.RunWith` ），所以如果使用ContextConfiguration注解进行测试，则需要引入@ExtendWith（`org.junit.jupiter.api.extension.ExtendWith`）注解。

  ```java
  @ExtendWith(SpringExtension.class)
  @ContextConfiguration(classes = InMemoryUserService.class)
  //@SpringJUnitConfig(classes = InMemoryUserService.class)
  public class UserServiceJUnit5Test {
  
      @Autowired
      private UserService userService;
  } 
  ```

  - JUnit4与Spring的整合

  ```java
  @RunWith(SpringRunner.class)
  @ContextConfiguration(classes = InMemoryUserService.class)
  public class UserServiceJUnit4Test {
  
      @Autowired
      private UserService userService;
  }    
  ```

- @SpringJUnitConfig

  -  `org.springframework.test.context.junit.jupiter.SpringJUnitConfig`
  - 整合了@ExtendWith和@ContextConfiguration ，源码如下：

  ```java
  @ExtendWith(SpringExtension.class)
  @ContextConfiguration
  @Documented
  @Inherited
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE)
  public @interface SpringJUnitConfig {
  }
  ```

  - 使用方法如下：

  ```java
  @SpringJUnitConfig(classes = InMemoryUserService.class)
  public class UserServiceJUnit5Test {
  
      @Autowired
      private UserService userService;
      
   }   
  ```


#### Spring WebMVC Test Framework

- @SpringJunitWebConfig
- @RestTemplate

#### Mockito整合

## 常见问题

### IDE问题

根据[官方文档](https://junit.org/junit5/docs/current/user-guide/#running-tests-ide)说明，使用JUnit5需要使用Idea2017.3以后的版本，否则会出现意想不到的问题。比如，明明已经引入了相关的Jar包，运行时出现 `org.junit.platform.commons.util.ReflectionUtils`类中的NoSuchMethod等异常。

> ```
> IntelliJ IDEA releases prior to IDEA 2017.3 bundle specific versions of JUnit 5. Thus, if you want to use a newer version of JUnit Jupiter, execution of tests within the IDE might fail due to version conflicts. In such cases, please follow the instructions below to use a newer version of JUnit 5 than the one bundled with IntelliJ IDEA.
> ```

### JDK版本问题

#### 前提

- Junit相关的MAVEN依赖如下：

```xml
<dependency>
    <groupId>org.junit.platform</groupId>
    <artifactId>junit-platform-launcher</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.junit.vintage</groupId>
    <artifactId>junit-vintage-engine</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-api</artifactId>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-params</artifactId>
    <scope>test</scope>
</dependency>
```

- Spring版本： `<spring.version>5.1.4.RELEASE</spring.version>`
- JDK版本：JDK_1.8_0_11
- Tomcat版本：TOMCAT_9_0_14

#### 测试

如果使用JDK_1.8_0_11版本，则运行如下Mock测试的时候，会出现异常。

```java
 @Test
 public void testHttpServletRequestInDynamicMock() {
     // 动态代理 HttpServletRequest
     HttpServletRequest request= mock(HttpServletRequest.class);
     // 当需要调用 HttpServletRequest#getParameter 时，并且参数名称为"name"
     when(request.getParameter("name")).thenReturn("Landy");

    String value = request.getParameter("name");

    assertEquals("Landy",value);
}
```

异常如下：

```
org.mockito.exceptions.base.MockitoException: 
Mockito cannot mock this class: interface javax.servlet.http.HttpServletRequest.

Mockito can only mock non-private & non-final classes.
If you're not sure why you're getting this error, please report to the mailing list.


Java               : 1.8
JVM vendor name    : Oracle Corporation
JVM vendor version : 25.11-b03
JVM name           : Java HotSpot(TM) 64-Bit Server VM
JVM version        : 1.8.0_11-b12
JVM info           : mixed mode
OS name            : Windows 8.1
OS version         : 6.3


Underlying exception : java.lang.IllegalArgumentException: object is not an instance of declaring class
```

#### 解决

升级JDK版本至jdk1.8.0_202即可解决问题。再次运行就一切ok。

#### 原因

猜测可能是由于，我本地使用的环境是用Spring Boot2搭建的，他引入的Maven如下：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.2.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

这样可以知道它所依赖的tomcat插件版本为tomcat9，根据如下规范对应表格，我们可以知道，HttpServletRequest依赖的JDK版本需要1.8.0_181以上。

| Tomcat版本 | Servlet规范 | JDK版本                        |
| ---------- | ----------- | ------------------------------ |
| Tomcat 6.x | Servlet 2.5 | JDK1.5+                        |
| Tomcat 7.x | Servlet 3.0 | JDK1.6+(Web Socket需要JDK1.7+) |
| Tomcat 8.x | Servlet 3.1 | JDK1.7+                        |
| Tomcat 9.x | Servlet 4   | JDK1.8+                        |

通过查看tomcat插件的MANIFEST.MF文件可以知道JDK的具体版本为1.8.0_181。

```
Bundle-Name: TOMCAT_9_0_14
Bundle-SymbolicName: TOMCAT_9_0_14
Bundle-Version: 0
Created-By: 1.8.0_181 (Oracle Corporation)
DSTAMP: 20181206
Implementation-Title: Apache Tomcat
Implementation-Vendor: Apache Software Foundation
Implementation-Version: 9.0.14
```

### 引用链接

- JUnit5： https://junit.org/junit5/docs/current/user-guide/ 
- Spring5 testing：https://docs.spring.io/spring/docs/5.1.4.RELEASE/spring-framework-reference/testing.html 

