# Spring Framework

## 多JDK版本说明

以前安装JDK，需要手动配置环境变量。JDK8以后多了自动配置环境变量，所以可以不用手动配置。如果我已经装了JDK8，还想再装一个JDK9，安装完，自动配置的环境变量会指向JDK9版本。

### 解决方法

#### 1. 删除自动配置的环境变量 

自动配置的环境变量是一个隐藏目录：`C:\ProgramData\Oracle\Java\javapath`，删掉这个目录下的3个exe文件，系统就无法匹配到了。 

#### 2. 手动配置环境变量 

自动匹配是匹配不到了，所以我们用老办法，手动配置环境变量即可。这样，我们可以根据环境变量里配置的JDK版本去实现版本切换了。

## Tutorial

* [Spring-framework-4.x](https://github.com/landy8530/spring-framework-lesson/wiki/Spring-Framework-4.x)
  * [1. Spring Annotation驱动编程](https://github.com/landy8530/spring-framework-lesson/wiki/Spring-Annotation%E9%A9%B1%E5%8A%A8%E7%BC%96%E7%A8%8B)
  * [2. Java Beans内省机制以及在Spring中的应用](https://github.com/landy8530/spring-framework-lesson/wiki/Java-Beans%E5%86%85%E7%9C%81%E6%9C%BA%E5%88%B6%E4%BB%A5%E5%8F%8A%E5%9C%A8Spring%E4%B8%AD%E7%9A%84%E5%BA%94%E7%94%A8) 
  * [3. Java资源管理以及在Spring中的应用](https://github.com/landy8530/spring-framework-lesson/wiki/Java%E8%B5%84%E6%BA%90%E7%AE%A1%E7%90%86%E4%BB%A5%E5%8F%8A%E5%9C%A8Spring%E4%B8%AD%E7%9A%84%E5%BA%94%E7%94%A8) 
  * [4. Spring自定义XML配置扩展](https://github.com/landy8530/spring-framework-lesson/wiki/Spring%E8%87%AA%E5%AE%9A%E4%B9%89XML%E9%85%8D%E7%BD%AE%E6%89%A9%E5%B1%95) 

* [Spring-framework-5.x](https://github.com/landy8530/spring-framework-lesson/wiki/Spring-Framework-5.x)
  * [1. 深入Java之国际化](https://github.com/landy8530/spring-framework-lesson/wiki/%E6%B7%B1%E5%85%A5Java%E4%B9%8B%E5%9B%BD%E9%99%85%E5%8C%96) 
  * [2. JSP在Spring中的应用(Annotation版)](https://github.com/landy8530/spring-framework-lesson/wiki/JSP%E5%9C%A8Spring%E4%B8%AD%E7%9A%84%E5%BA%94%E7%94%A8) 
  * [3. JSP在Spring中的应用（XML版）](https://github.com/landy8530/spring-framework-lesson/wiki/JSP%E5%9C%A8Spring%E4%B8%AD%E7%9A%84%E5%BA%94%E7%94%A8) 
  * [4. Java Reactive Web设计与实现](https://github.com/landy8530/spring-framework-lesson/wiki/Java-Reactive-Web%E8%AE%BE%E8%AE%A1%E4%B8%8E%E5%AE%9E%E7%8E%B0) 
  * [5. Servlet在Spring中的应用](https://github.com/landy8530/spring-framework-lesson/wiki/Servlet%E5%9C%A8Spring%E4%B8%AD%E7%9A%84%E5%BA%94%E7%94%A8) 
  * [6. Spring5新特性之测试](https://github.com/landy8530/spring-framework-lesson/wiki/Spring5%E6%96%B0%E7%89%B9%E6%80%A7%E4%B9%8B%E6%B5%8B%E8%AF%95) 
  * [7. Spring5新特性之Web Flux](https://github.com/landy8530/spring-framework-lesson/wiki/Spring5%E6%96%B0%E7%89%B9%E6%80%A7%E4%B9%8BWeb-Flux) 
  * [8. Spring Web自动装配（Annotation）](https://github.com/landy8530/spring-framework-lesson/wiki/Spring-Web%E8%87%AA%E5%8A%A8%E8%A3%85%E9%85%8D) 

* [Spring-framework-common](https://github.com/landy8530/spring-framework-lesson/wiki/Spring-framework-common)
  * [1. Spring Aware接口应用](https://github.com/landy8530/spring-framework-lesson/wiki/Spring-Aware%E6%8E%A5%E5%8F%A3%E5%BA%94%E7%94%A8) 