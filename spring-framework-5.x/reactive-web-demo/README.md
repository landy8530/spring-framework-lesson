# Java Reactive Web设计与实现

## 0. 编程模型与并发模型

Spring 5实现了一部分Reactive

Spring WebFlux： Reactive Web（non-blocking servers in general）

Spring Web MVC：传统Servlet Web（servlet applications in general）

### 0.1 编程模型

编程模型：阻塞、非阻塞

- NIO：同步+非阻塞，基于事件
- 非阻塞
  - 基本上采用Callback方式
  - 当时不阻塞，后续再输出（再回调）

### 0.2 并发模型

- 并发模型：
  - 同步（Sync）
  - 异步（Async）

### 0.3 比较

- 同步+非阻塞：线程不会改变，不会切换

```
[线程：main] Observable 添加观察者！ 
[线程：main] 通知所有观察者！ 
[线程：main] 3. 收到数据更新：Hello World 
[线程：main] 2. 收到数据更新：Hello World 
[线程：main] 1. 收到数据更新：Hello World 
```

- 异步+非阻塞：线程会被切换

```
[线程：main] 启动一个JFrame窗口！ 
[线程：AWT-EventQueue-0] 销毁当前窗口！ 
[线程：AWT-EventQueue-0] 窗口被关闭，退出程序！
```

使用Jconsole查看改异步非阻塞程序

等待总数一直在增加，说明异步程序一直在等待。NIO就是无限地在处理，无限地在等待。

## 1. Reactive概念

Reactive Programming：响应式编程，异步非阻塞就是响应式编程(Reactive Programming)，与之相对应的是命令式编程。

Reactive并不是一种新的技术，不用Reactive照样可以实现非阻塞（同步、异步均可，推拉模式的结合），比如利用观察者模式实现（比如Java Swing GUI技术）。

Reactive的另外一种实现方式就是消息队列。

### 1.1 标准概念

#### 1.1.1 维基百科讲法

https://en.wikipedia.org/wiki/Reactive_programming 

> In [computing](https://en.wikipedia.org/wiki/Computing), **reactive programming** is a declarative [programming paradigm](https://en.wikipedia.org/wiki/Programming_paradigm) concerned with [data streams](https://en.wikipedia.org/wiki/Dataflow_programming) and the propagation of change. With this paradigm it is possible to express static (e.g., arrays) or dynamic (e.g., event emitters) *data streams* with ease, and also communicate that an inferred dependency within the associated *execution model* exists, which facilitates the automatic propagation of the changed data flow

关键点：

- 声明式的编程范式

- 数据流
- 传播改变
- 使用静态数组或者动态事件

#### 1.1.2 Reactive-Streams讲法

Reactive标准：http://www.reactive-streams.org/ 

Reactive Streams JVM实现：https://github.com/reactive-streams/reactive-streams-jvm/tree/v1.0.2

> Handling streams of data—especially “live” data whose volume is not predetermined—requires special care in an asynchronous system. The most prominent issue is that resource consumption needs to be carefully controlled such that a fast data source does not overwhelm the stream destination. Asynchrony is needed in order to enable the parallel use of computing resources, on collaborating network hosts or multiple CPU cores within a single machine.

关键点：

- 处理的是数据流（活跃数据，动态数据）
- 数据容量无法预判

- 异步系统
- 资源消费需要精细控制
- data source（数据上游），stream destination（数据下游）

> In summary, Reactive Streams is a standard and specification for Stream-oriented libraries for the JVM that
>
> - process a potentially unbounded number of elements
> - in sequence,
> - asynchronously passing elements between components,
> - with mandatory non-blocking backpressure.

### 1.2 实现框架

#### 1.2.1 ReactiveX

ReactiveX：http://reactivex.io/intro.html

> ReactiveX is a library for composing asynchronous and event-based programs by using observable sequences.
>
> It extends [the observer pattern](http://en.wikipedia.org/wiki/Observer_pattern) to support sequences of data and/or events and adds operators that allow you to compose sequences together declaratively while abstracting away concerns about things like low-level threading, synchronization, thread-safety, concurrent data structures, and non-blocking I/O.

- 是一个类库
- 异步
- 基于事件
- 可观察的数据/事件序列（sequences of data and/or events）
  - 有顺序的
  - 可增长的
- 设计模式上说：继承了观察者模式
- 数据结构上说：支持数据序列（sequences of data），其实就是数据流（streams of data），并且需要屏蔽高并发相关（线程，同步，线程安全，并发数据结构，非阻塞I/O等）

> You can think of the Observable class as a “push” equivalent to [Iterable](http://docs.oracle.com/javase/7/docs/api/java/lang/Iterable.html), which is a “pull.” With an Iterable, the consumer pulls values from the producer and the thread blocks until those values arrive. 

- Observable观察者模式是“push”模式
- Iterable是“pull”模式
  - 数据已经准备好了，自己直接去“拉”即可
  - Iterable：For Each语句的提升
  - Iterator：迭代器（Java 5）

#### 1.2.2 Reactor

https://projectreactor.io/docs/core/release/reference/#intro-reactive

> Reactor is a fully non-blocking reactive programming foundation for the JVM, with efficient demand management (in the form of managing "backpressure"). It integrates directly with the Java 8 functional APIs, notably `CompletableFuture`, `Stream`, and `Duration`. It offers composable asynchronous sequence APIs `Flux` (for [N] elements) and `Mono` (for [0|1] elements), extensively implementing the [Reactive Streams](<http://www.reactive-streams.org/>) specification.
>
> Reactor also supports non-blocking inter-process communication with the `reactor-netty` project. Suited for Microservices Architecture, Reactor Netty offers backpressure-ready network engines for HTTP (including Websockets), TCP, and UDP. Reactive Encoding and Decoding are fully supported.

### 1.3 Spring WebFlux

https://docs.spring.io/spring/docs/5.1.5.RELEASE/spring-framework-reference/web-reactive.html#webflux-concurrency-model

> The original web framework included in the Spring Framework, Spring Web MVC, was purpose-built for the Servlet API and Servlet containers. The reactive-stack web framework, Spring WebFlux, was added later in version 5.0. It is fully non-blocking, supports [Reactive Streams](http://www.reactive-streams.org/) back pressure, and runs on such servers as Netty, Undertow, and Servlet 3.1+ containers.

- 可以运行在Netty，Undertow，Servlet 3.1+等容器中
- Servlet 3.1也有异步处理

#### 1.3.1 Why WebFlux？

> Part of the answer is the need for a non-blocking web stack to handle concurrency with a small number of threads and scale with fewer hardware resources. Servlet 3.1 did provide an API for non-blocking I/O. However, using it leads away from the rest of the Servlet API, where contracts are synchronous (`Filter`, `Servlet`) or blocking (`getParameter`, `getPart`). This was the motivation for a new common API to serve as a foundation across any non-blocking runtime. That is important because of servers (such as Netty) that are well-established in the async, non-blocking space.
>
> The other part of the answer is functional programming. Much as the addition of annotations in Java 5 created opportunities (such as annotated REST controllers or unit tests), the addition of lambda expressions in Java 8 created opportunities for functional APIs in Java. This is a boon for non-blocking applications and continuation-style APIs (as popularized by `CompletableFuture` and [ReactiveX](http://reactivex.io/)) that allow declarative composition of asynchronous logic. At the programming-model level, Java 8 enabled Spring WebFlux to offer functional web endpoints alongside annotated controllers.

- 异步非阻塞
  - 异步非阻塞（non-blocking web stack）
  - 少量线程数处理并发性（handle concurrency with a small number of threads）
  - 少量硬件资源提高伸缩性（scale with fewer hardware resources.）
  - Servlet 3.1 提供一个非阻塞API
- 函数式编程（functional programming）

#### 1.3.2 Define “Reactive”

> The term, “reactive,” refers to programming models that are built around reacting to change — network components reacting to I/O events, UI controllers reacting to mouse events, and others. In that sense, non-blocking is reactive, because, instead of being blocked, we are now in the mode of reacting to notifications as operations complete or data becomes available.
>
> There is also another important mechanism that we on the Spring team associate with “reactive” and that is non-blocking back pressure. In synchronous, imperative code, blocking calls serve as a natural form of back pressure that forces the caller to wait. In non-blocking code, it becomes important to control the rate of events so that a fast producer does not overwhelm its destination.
>
> Reactive Streams is a [small spec](https://github.com/reactive-streams/reactive-streams-jvm/blob/master/README.md#specification) (also [adopted](https://docs.oracle.com/javase/9/docs/api/java/util/concurrent/Flow.html) in Java 9) that defines the interaction between asynchronous components with back pressure. For example a data repository (acting as [Publisher](http://www.reactive-streams.org/reactive-streams-1.0.1-javadoc/org/reactivestreams/Publisher.html)) can produce data that an HTTP server (acting as [Subscriber](http://www.reactive-streams.org/reactive-streams-1.0.1-javadoc/org/reactivestreams/Subscriber.html)) can then write to the response. The main purpose of Reactive Streams is to let the subscriber to control how quickly or how slowly the publisher produces data.

- 指的是围绕对变化作出反应而构建的编程模型 
  - 对I / O事件做出反应的网络组件
  - 对鼠标事件做出反应的UI控制器等。
- 非阻塞在某种程度上说就是Reactive（In that sense, non-blocking is reactive）
- 非阻塞背压
- 小的规范

#### 1.3.2 Performance

> Performance has many characteristics and meanings. Reactive and non-blocking generally do not make applications run faster. They can, in some cases, (for example, if using the `WebClient` to execute remote calls in parallel). On the whole, it requires more work to do things the non-blocking way and that can increase slightly the required processing time.
>
> The key expected benefit of reactive and non-blocking is the ability to scale with a small, fixed number of threads and less memory. That makes applications more resilient under load, because they scale in a more predictable way. In order to observe those benefits, however, you need to have some latency (including a mix of slow and unpredictable network I/O). That is where the reactive stack begins to show its strengths, and the differences can be dramatic.

- 并不是为了变得更快
  - 一般而言，并不是为了使应用程序运行更快（Reactive and non-blocking generally do not make applications run faster）
  - 为了非阻塞需要额外的工作可能会增加处理时间（it requires more work to do things the non-blocking way and that can increase slightly the required processing time）
- 关键的一个好处（The key expected benefit）
  - 利用固定、少量的线程，小的内存提供伸缩性的能力（reactive and non-blocking is the ability to scale with a small, fixed number of threads and less memory）
- 某种程度上，WebFlux并不适合Web请求

### 1.4 特性

- 异步
- 非阻塞
- 事件驱动
- 可能有背压（back pressure）
- ~~多路复用~~
  - 这点是NIO中的特点，Reactive是整个编程范式的概念
- ~~高吞吐~~

### 1.5 目的

防止回调地狱（Callback Hell）

> Callbacks are hard to compose together, quickly leading to code that is difficult to read and maintain (known as "Callback Hell").

关于回调地域相关说明参见以下链接

https://projectreactor.io/docs/core/release/reference/#getting-started-introducing-reactor

> The second approach (mentioned earlier), seeking more efficiency, can be a solution to the resource wasting problem. By writing *asynchronous*, *non-blocking* code, you let the execution switch to another active task **using the same underlying resources** and later come back to the current process when the asynchronous processing has finished.
>
> But how can you produce asynchronous code on the JVM? Java offers two models of asynchronous programming:
>
> - **Callbacks**: Asynchronous methods do not have a return value but take an extra `callback` parameter (a lambda or anonymous class) that gets called when the result is available. A well known example is Swing’s `EventListener`hierarchy.
> - **Futures**: Asynchronous methods return a `Future<T>` **immediately**. The asynchronous process computes a `T` value, but the `Future` object wraps access to it. The value is not immediately available, and the object can be polled until the value is available. For instance, `ExecutorService` running `Callable<T>` tasks use `Future` objects.

## 2. Reactive使用场景

Long Live模式：Netty I/O连接（RPC）

Short Live模式：HTTP(需要超时时间)

短平快的应用并不适合Reactive，需要做合理的timeout时间规划

Reactive 性能测试： https://blog.ippon.tech/spring-5-webflux-performance-tests/

## 3. Reactive理解误区

Web：快速响应（Socket Connection Timeout）

Tomcat Connector Thread Pool（200个线程） --> Reactive Thread Pool(50个线程)

I/O连接从Tomcat转移到了Reactive

- Tomcat Thread 负责处理连接（可以由200个连接扩充到2000个连接）
- Reactive Thread负责处理任务（有可能会处理不过来）
  - 继续等待（Timeout无限）
  - Timeout

## 4. 设计Reactive Web

### 4.1 异步设计

利用Servlet 3.1规范中的异步处理（Asynchronous processing），异步上下文（AsyncContext）实现异步Servlet。

> Some times a filter and/or servlet is unableto complete the processing of a request 
> without waiting for a resource or event before generating a response. For example, a 
> servlet may need to wait for an available JDBC connection, for a response from a 
> remote web service, for a JMS message, or for an application event, before 
> proceeding to generate a response. Waiting within the servlet is an inefficient 
> operation as it is a blocking operation that consumes a thread and other limited 
> resources. Frequently a slow resource such as a database may have many threads 
> blocked waiting for access and can cause thread starvation and poor quality of 
> service for an entire web container.
> The asynchronous processing of requests is introduced to allow the thread may 
> return to the container and perform other tasks. When asynchronous processing 
> begins on the request, another thread or callback may either generate the response 
> and call completeor dispatch the request so that it may run in the context of the 
> container using the AsyncContext.dispatchmethod. A typical sequence of events 
> for asynchronous processing is:
> 1. The request is received and passed via normal filters for authentication etc. to the 
>   servlet.
> 2. The servlet processes the request parameters and/or content to determine the 
>    nature of the request.
> 3. The servlet issues requests for resources or data, for example, sends a remote web 
>    service request or joins a queue waiting for a JDBC connection.
> 4. The servlet returns without generating a response.
> 5. After some time, the requested resourcebecomes available, the thread handling 
>    that event continues processing either in the same thread or by dispatching to a 
>    resource in the container using the AsyncContext.

### 4.2 非阻塞设计

利用Servlet 3.1规范中的Non Blocking IO

> Non-blocking request processing in the Web Container helps improve the ever 
> increasing demand for improved Web Container scalability, increase the number of 
> connections that can simultaneously be handled by the Web Container. Nonblocking IO in the Servlet container allowsdevelopers to readdata as it becomes 
> available or write data when possible to do so. Non-blocking IO only works with 
> async request processing in Servlets and Filters (as defined in Section 2.3.3.3, 
> “Asynchronous processing” on page 2-10), and upgrade processing (as defined in 
> Section 2.3.3.5, “Upgrade Processing” on page 2-20). Otherwise, an 
> IllegalStateExceptionmust be thrown when 
> ServletInputStream.setReadListeneror 
> ServletOutputStream.setWriteListeneris invoked.

## 5. 实现Reactive Web

