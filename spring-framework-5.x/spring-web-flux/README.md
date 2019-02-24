# Table of Contents

* [Spring5新特性之Web Flux](#spring5新特性之web-flux)
  * [1. 为什么要使用Web Flux？](#1-为什么要使用web-flux？)
  * [2. 从Web MVC 过度到Web Flux](#2-从web-mvc-过度到web-flux)
    * [2.1 Annotated Controller](#21-annotated-controller)
    * [2.2 Web Flux配置](#22-web-flux配置)
    * [2.3 Reactor框架](#23-reactor框架)
  * [3. 函数式Endpoint](#3-函数式endpoint)

# Spring5新特性之Web Flux

## 1. 为什么要使用Web Flux？

摘自：https://docs.spring.io/spring/docs/5.1.4.RELEASE/spring-framework-reference/web-reactive.html#spring-webflux 

- Non-blocking Programing
  - NIO
  - Reactive

```
Part of the answer is the need for a non-blocking web stack to handle concurrency with a small number of threads and scale with fewer hardware resources. Servlet 3.1 did provide an API for non-blocking I/O
```

- Functional Programing
  - Lambda

```
The other part of the answer is functional programming.Much as the addition of annotations in Java 5 created opportunities (such as annotated REST controllers or unit tests), the addition of lambda expressions in Java 8 created opportunities for functional APIs in Java.
```



## 2. 从Web MVC 过度到Web Flux

注意：Spring Web MVC和Web Flux是不能共存的。他们都不是线程安全的？

Spring Web MVC 是“抄袭”JSR规范而来的。

三流公司做产品，二流公司做技术，一流公司做规范。

Web Flux启动的是NettyWebServer，没有Servlet规范了，不依赖于Servlet容器了。Web MVC是依赖于Servlet规范的。

除了传统的请求外，还可以用Netty容器了。

### 2.1 Annotated Controller

跟Spring Web MVC是一致的。

### 2.2 Web Flux配置

### 2.3 Reactor框架

Mono是Reactor中的一个概念，跟JDK中的Optional是一个概念。

## 3. 函数式Endpoint