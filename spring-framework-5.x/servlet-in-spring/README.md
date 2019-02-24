# Table of Contents

* [Servlet在Spring中的应用](#servlet在spring中的应用)
  * [1. Servlet 介绍](#1-servlet-介绍)
    * [1.1 Servlet API规范](#11-servlet-api规范)
    * [1.2 Spring Web Flux](#12-spring-web-flux)
  * [2. Servlet 组件](#2-servlet-组件)
    * [2.1 Servlet](#21-servlet)
    * [2.2 Filter](#22-filter)
    * [2.3 Listener](#23-listener)
  * [3. Servlet在Spring中的应用](#3-servlet在spring中的应用)
    * [3.1 Dispatcher Servlet](#31-dispatcher-servlet)
      * [3.1.1 Front Controller](#311-front-controller)
      * [3.1.2 Root WebApplicationContext](#312-root-webapplicationcontext)
      * [3.1.3 Servlet WebApplicationContext](#313-servlet-webapplicationcontext)

# Servlet在Spring中的应用

## 1. Servlet 介绍

Servlet是一种基于Java的平台无关的Web组件，由Java Web服务器加载执行。

- 从功能上，介于CGI（Common Gate Interface）和功能扩展（Apache模块）之间
- 从体系上，Servlet技术（规范）属于Java EE的一部分

### 1.1 Servlet API规范

来自wikipedia：https://en.wikipedia.org/wiki/Java_servlet#History 

| Servlet API version | Released                                                     | Specification                                                | Platform             | Important Changes                                            |
| ------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | -------------------- | ------------------------------------------------------------ |
| Servlet 4.0         | Sep 2017                                                     | [JSR 369](https://jcp.org/en/jsr/detail?id=369)              | Java EE 8            | [HTTP/2](https://en.wikipedia.org/wiki/HTTP/2)               |
| Servlet 3.1         | May 2013                                                     | [JSR 340](https://jcp.org/en/jsr/detail?id=340)              | Java EE 7            | Non-blocking I/O, HTTP protocol upgrade mechanism ([WebSocket](https://en.wikipedia.org/wiki/WebSocket))[[14\]](https://en.wikipedia.org/wiki/Java_servlet#cite_note-14) |
| Servlet 3.0         | [December 2009](http://www.javaworld.com/javaworld/jw-02-2009/jw-02-servlet3.html) | [JSR 315](https://jcp.org/en/jsr/detail?id=315)              | Java EE 6, Java SE 6 | Pluggability, Ease of development, Async Servlet, Security, File Uploading |
| Servlet 2.5         | [September 2005](http://www.javaworld.com/javaworld/jw-01-2006/jw-0102-servlet.html) | [JSR 154](https://jcp.org/en/jsr/detail?id=154)              | Java EE 5, Java SE 5 | Requires Java SE 5, supports annotation                      |
| Servlet 2.4         | [November 2003](http://www.javaworld.com/jw-03-2003/jw-0328-servlet.html) | [JSR 154](https://jcp.org/en/jsr/detail?id=154)              | J2EE 1.4, J2SE 1.3   | web.xml uses XML Schema                                      |
| Servlet 2.3         | [August 2001](http://www.javaworld.com/jw-01-2001/jw-0126-servletapi.html) | [JSR 53](https://jcp.org/en/jsr/detail?id=53)                | J2EE 1.3, J2SE 1.2   | Addition of `Filter`                                         |
| Servlet 2.2         | [August 1999](http://www.javaworld.com/jw-10-1999/jw-10-servletapi.html) | [JSR 902](https://jcp.org/en/jsr/detail?id=902), [JSR 903](https://jcp.org/en/jsr/detail?id=903) | J2EE 1.2, J2SE 1.2   | Becomes part of J2EE, introduced independent web applications in .war files |
| Servlet 2.1         | [November 1998](http://www.javaworld.com/jw-12-1998/jw-12-servletapi.html) | [2.1a](https://web.archive.org/web/20090611171402/http://java.sun.com:80/products/servlet/2.1/servlet-2.1.pdf) | Unspecified          | First official specification, added `RequestDispatcher`, `ServletContext` |
| Servlet 2.0         | December 1997                                                | N/A                                                          | JDK 1.1              | Part of April 1998 Java Servlet Development Kit 2.0[[15\]](https://en.wikipedia.org/wiki/Java_servlet#cite_note-15) |
| Servlet 1.0         | December 1996                                                | N/A                                                          |                      | Part of June 1997 Java Servlet Development Kit (JSDK) 1.0[[9\]](https://en.wikipedia.org/wiki/Java_servlet#cite_note-Hunter200003-9) |

### 1.2 Spring Web Flux

https://docs.spring.io/spring/docs/5.1.4.RELEASE/spring-framework-reference/web-reactive.html#spring-webflux

- NIO
  - Part of the answer is the need for a non-blocking web stack to handle concurrency with a small number of threads and scale with fewer hardware resources. Servlet 3.1 did provide an API for non-blocking I/O.
- Functional Programming
  - The other part of the answer is functional programming.

## 2. Servlet 组件

- Authentication Filters

- Logging and auditing filters

- Image conversions filters

- Data compression filters

- Encryption filters

- Tokenizing filters

### 2.1 Servlet

### 2.2 Filter

### 2.3 Listener

- ServletContextListener

- HttpSessionListener

   

## 3. Servlet在Spring中的应用

### 3.1 Dispatcher Servlet

类继承关系如下：

```
java.lang.Object
  javax.servlet.GenericServlet
      javax.servlet.http.HttpServlet
          org.springframework.web.servlet.HttpServletBean
              org.springframework.web.servlet.FrameworkServlet
                  org.springframework.web.servlet.DispatcherServlet
```

#### 3.1.1 Front Controller

在[Spring1.0的API](https://docs.spring.io/spring/docs/1.0.0/javadoc-api/) 文档中可以发现 Dispatcher Servlet是一个总的前端总控制器。

> Concrete front controller for use within the web MVC framework. Dispatches to registered handlers for processing a web request.

关于前端控制器请参考：http://www.corej2eepatterns.com/FrontController.htm

#### 3.1.2 Root WebApplicationContext

```xml
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>/WEB-INF/app-context.xml</param-value>
</context-param>


<servlet>
    <servlet-name>servlet-in-spring</servlet-name>
    <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value></param-value>
    </init-param>
    <init-param>
        <param-name>namespace</param-name>
        <param-value>landy</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>

<servlet-mapping>
    <servlet-name>servlet-in-spring</servlet-name>
    <url-pattern>/*</url-pattern>
</servlet-mapping>
```

根据以上web.xml配置，可知启动容器的时候，首先要先初始化Spring Root WebApplicationContext对象，其由 `org.springframework.web.context.ContextLoaderListener`实现，其实现了Servlet API中的ServletContextListener接口，定义如下：

```java
public class ContextLoaderListener extends ContextLoader implements ServletContextListener {
}
```

初始化方法如下：

```java
/**
* Initialize the root web application context.
*/
@Override
public void contextInitialized(ServletContextEvent event) {
	initWebApplicationContext(event.getServletContext());
}
```

然后委托给父类ContextLoader实现，即`org.springframework.web.context.ContextLoader#initWebApplicationContext`方法，定义如下：

```java
/**
	 * Initialize Spring's web application context for the given servlet context,
	 * using the application context provided at construction time, or creating a new one
	 * according to the "{@link #CONTEXT_CLASS_PARAM contextClass}" and
	 * "{@link #CONFIG_LOCATION_PARAM contextConfigLocation}" context-params.
	 * @param servletContext current servlet context
	 * @return the new WebApplicationContext
	 * @see #ContextLoader(WebApplicationContext)
	 * @see #CONTEXT_CLASS_PARAM
	 * @see #CONFIG_LOCATION_PARAM
	 */
	public WebApplicationContext initWebApplicationContext(ServletContext servletContext) {
		......
		try {
			// Store context in local instance variable, to guarantee that
			// it is available on ServletContext shutdown.
			if (this.context == null) {
				this.context = createWebApplicationContext(servletContext);
			}
			if (this.context instanceof ConfigurableWebApplicationContext) {
				ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) this.context;
				if (!cwac.isActive()) {
					// The context has not yet been refreshed -> provide services such as
					// setting the parent context, setting the application context id, etc
					if (cwac.getParent() == null) {
						// The context instance was injected without an explicit parent ->
						// determine parent for root web application context, if any.
						ApplicationContext parent = loadParentContext(servletContext);
						cwac.setParent(parent);
					}
					configureAndRefreshWebApplicationContext(cwac, servletContext);
				}
			}
			servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);

			......

			return this.context;
		}
		......
	}
```

以上方法说明，先去通过 `org.springframework.web.context.ContextLoader#createWebApplicationContext` 创建WebApplicationContext，然后判断该WebApplicationContext是否是实现了`ConfigurableWebApplicationContext`，如果是，则需要把相关的applicationContext.xml配置信息（即web.xml中配置的contextConfigLocation）加载到ApplicationContext中，并且执行刷新操作，即执行 `org.springframework.web.context.ContextLoader#configureAndRefreshWebApplicationContext`方法。

注意：在执行createWebApplicationContext方法的时候，会先调用 `Class<?> contextClass = determineContextClass(sc);`方法查找到相应的WebApplicationContext的实现类。定义如下：

```java
/**
	 * Return the WebApplicationContext implementation class to use, either the
	 * default XmlWebApplicationContext or a custom context class if specified.
	 * @param servletContext current servlet context
	 * @return the WebApplicationContext implementation class to use
	 * @see #CONTEXT_CLASS_PARAM
	 * @see org.springframework.web.context.support.XmlWebApplicationContext
	 */
	protected Class<?> determineContextClass(ServletContext servletContext) {
		String contextClassName = servletContext.getInitParameter(CONTEXT_CLASS_PARAM);
		if (contextClassName != null) {
			try {
				return ClassUtils.forName(contextClassName, ClassUtils.getDefaultClassLoader());
			}
			catch (ClassNotFoundException ex) {
				throw new ApplicationContextException(
						"Failed to load custom context class [" + contextClassName + "]", ex);
			}
		}
		else {
			contextClassName = defaultStrategies.getProperty(WebApplicationContext.class.getName());
			try {
				return ClassUtils.forName(contextClassName, ContextLoader.class.getClassLoader());
			}
			catch (ClassNotFoundException ex) {
				throw new ApplicationContextException(
						"Failed to load default context class [" + contextClassName + "]", ex);
			}
		}
	}
```

由以上定义可知，如果未在web.xml中配置contextClass选项，则使用默认的实现类XmlWebApplicationContext进行初始化。

默认的实现类由ContextLoader.properties文件配置而来，然后由静态代码块加载至defaultStrategies对象中，Spring默认配置如下：

```properties
# Default WebApplicationContext implementation class for ContextLoader.
# Used as fallback when no explicit context implementation has been specified as context-param.
# Not meant to be customized by application developers.

org.springframework.web.context.WebApplicationContext=org.springframework.web.context.support.XmlWebApplicationContext
```

至此，Root WebApplicationContext加载完毕。

#### 3.1.3 Servlet WebApplicationContext

根据前文所述，Servlet WebApplicationContext的父节点就是Root WebApplicationContext（即ContextLoaderListener），那么它如何加载的呢？

根据Web.xml配置信息可知，通过 `org.springframework.web.servlet.DispatcherServlet`这个servlet进行相应的初始化操作。

根据Servlet规范，可以得知初始化的方法链如下进行：

`javax.servlet.Servlet#init` -> `javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)` ->

`javax.servlet.GenericServlet#init()` ->

`org.springframework.web.servlet.HttpServletBean#init` 

方法定义如下：

```java
@Override
	public final void init() throws ServletException {
		.....

		// Set bean properties from init parameters.
		try {
			PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);
			BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
			ResourceLoader resourceLoader = new ServletContextResourceLoader(getServletContext());
			bw.registerCustomEditor(Resource.class, new ResourceEditor(resourceLoader, getEnvironment()));
			initBeanWrapper(bw);
			bw.setPropertyValues(pvs, true);
		}
		catch (BeansException ex) {
			logger.error("Failed to set bean properties on servlet '" + getServletName() + "'", ex);
			throw ex;
		}

		// Let subclasses do whatever initialization they like.
		initServletBean();

		......
	}
```

由如上代码可知，初始化ServletBean的操作交由initServletBean方法，由其子类实现的相应的方法，`org.springframework.web.servlet.FrameworkServlet#initServletBean`

```java
/**
	 * Overridden method of {@link HttpServletBean}, invoked after any bean properties
	 * have been set. Creates this servlet's WebApplicationContext.
	 */
	@Override
	protected final void initServletBean() throws ServletException {
		......
		try {
			this.webApplicationContext = initWebApplicationContext();
			initFrameworkServlet();
		}
		...... 
	}
```

根据他的Java Doc注释就可以得知，是进行初始化 Servlet WebApplicationContext的。

```java
/**
	 * Initialize and publish the WebApplicationContext for this servlet.
	 * <p>Delegates to {@link #createWebApplicationContext} for actual creation
	 * of the context. Can be overridden in subclasses.
	 * @return the WebApplicationContext instance
	 * @see #FrameworkServlet(WebApplicationContext)
	 * @see #setContextClass
	 * @see #setContextConfigLocation
	 */
	protected WebApplicationContext initWebApplicationContext() {
        //通过ServletContext获取root WebApplicationContext
		WebApplicationContext rootContext =
		WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		WebApplicationContext wac = null;

        ......
		
		if (wac == null) {
			// No context instance was injected at construction time -> see if one
			// has been registered in the servlet context. If one exists, it is assumed
			// that the parent context (if any) has already been set and that the
			// user has performed any initialization such as setting the context id
			wac = findWebApplicationContext();
		}
		if (wac == null) {
			// No context instance is defined for this servlet -> create a local one
			wac = createWebApplicationContext(rootContext);
		}

		if (!this.refreshEventReceived) {
			// Either the context is not a ConfigurableApplicationContext with refresh
			// support or the context injected at construction time had already been
			// refreshed -> trigger initial onRefresh manually here.
			onRefresh(wac);
		}

		if (this.publishContext) {
			// Publish the context as a servlet context attribute.
			String attrName = getServletContextAttributeName();
			getServletContext().setAttribute(attrName, wac);
			if (this.logger.isDebugEnabled()) {
				this.logger.debug("Published WebApplicationContext of servlet '" + getServletName() +
						"' as ServletContext attribute with name [" + attrName + "]");
			}
		}

		return wac;
	}
```

进行追踪代码可知，Servlet WebApplicationContext的默认实现对象为 `org.springframework.web.context.support.XmlWebApplicationContext`

```java
/**
	 * Instantiate the WebApplicationContext for this servlet, either a default
	 * {@link org.springframework.web.context.support.XmlWebApplicationContext}
	 * or a {@link #setContextClass custom context class}, if set.
	 * <p>This implementation expects custom contexts to implement the
	 * {@link org.springframework.web.context.ConfigurableWebApplicationContext}
	 * interface. Can be overridden in subclasses.
	 * <p>Do not forget to register this servlet instance as application listener on the
	 * created context (for triggering its {@link #onRefresh callback}, and to call
	 * {@link org.springframework.context.ConfigurableApplicationContext#refresh()}
	 * before returning the context instance.
	 * @param parent the parent ApplicationContext to use, or {@code null} if none
	 * @return the WebApplicationContext for this servlet
	 * @see org.springframework.web.context.support.XmlWebApplicationContext
	 */
	protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
		Class<?> contextClass = getContextClass();
		.....
		ConfigurableWebApplicationContext wac =
				(ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);

		wac.setEnvironment(getEnvironment());
		wac.setParent(parent);
		wac.setConfigLocation(getContextConfigLocation());

		configureAndRefreshWebApplicationContext(wac);

		return wac;
	}
```

