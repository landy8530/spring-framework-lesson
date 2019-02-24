# Table of Contents

* [Spring自定义XML配置扩展](#spring自定义xml配置扩展)
  * [1. Spring XML配置扩展机制](#1-spring-xml配置扩展机制)
    * [1.1 XML技术简介](#11-xml技术简介)
    * [1.2 Spring Bean生命周期](#12-spring-bean生命周期)
    * [1.3 Spring XML配置扩展总结](#13-spring-xml配置扩展总结)
  * [2. Spring Framework内建实现](#2-spring-framework内建实现)
    * [2.1 Spring版本对应处理方式](#21-spring版本对应处理方式)
    * [2.2 Spring 配置placeholder的两种方式](#22-spring-配置placeholder的两种方式)
      * [2.2.1 利用bean标签配置](#221-利用bean标签配置)
      * [2.2.2 利用<context:property-placeholder> 标签](#222-利用contextproperty-placeholder-标签)
      * [2.2.3 两种方式优劣性](#223-两种方式优劣性)
    * [2.3 Schema配置](#23-schema配置)
    * [2.4 Namespace Handler配置](#24-namespace-handler配置)
    * [2.5 Bean解析](#25-bean解析)
  * [3. 自定义XML配置扩展](#3-自定义xml配置扩展)
    * [3.1 定义Schema](#31-定义schema)
    * [3.2 建立META-INF/spring.schemas](#32-建立meta-infspringschemas)
    * [3.3 添加 ehi.xsd 到 context.xml](#33-添加-ehixsd-到-contextxml)
    * [3.4 建立META-INF/spring.handlers](#34-建立meta-infspringhandlers)

# Spring自定义XML配置扩展

本文以Spring 4.3.4版本为例

## 1. Spring XML配置扩展机制

### 1.1 XML技术简介

- DOM --> Document Object Model（Tree技术）
  - 属性结构，好理解，性能最差
  - DOM技术在Java中的以 `org.w3c.dom.Document` 实现。

- SAX -> Simple API for XML（Event技术）
  - 文本处理，性能好
  - Spring5中的Marshalling技术就是利用SAX Handler实现

- XML Stream -> BEA 公司（Stream技术）
  - 时间处理，性能良好，相对好理解
  - `java.util.stream.Stream` 

- JAXB -> Java API XML Binding （Spring2.0后采用的方式）
  - 面向对象，性能良好，好理解

### 1.2 Spring Bean生命周期

BeanDefinition揭示了Spring Bean 生命周期的处理，接下来以一个事例代码展开Spring Bean生命周期的源码解析。事例代码如下：

```java
public class SpringDemo {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context =
                new ClassPathXmlApplicationContext("context.xml");

        User user = context.getBean("user1",User.class);
        System.out.println(user);

    }
}
```

首先，第一行代码，是ApplicationContext初始化的过程，利用context.xml资源文件初始化ApplicationContext。

```java
/**
	 * Create a new ClassPathXmlApplicationContext, loading the definitions
	 * from the given XML file and automatically refreshing the context.
	 * @param configLocation resource location
	 * @throws BeansException if context creation failed
	 */
	public ClassPathXmlApplicationContext(String configLocation) throws BeansException {
		this(new String[] {configLocation}, true, null);
	}

	/**
	 * Create a new ClassPathXmlApplicationContext with the given parent,
	 * loading the definitions from the given XML files.
	 * @param configLocations array of resource locations
	 * @param refresh whether to automatically refresh the context,
	 * loading all bean definitions and creating all singletons.
	 * Alternatively, call refresh manually after further configuring the context.
	 * @param parent the parent context
	 * @throws BeansException if context creation failed
	 * @see #refresh()
	 */
	public ClassPathXmlApplicationContext(String[] configLocations, boolean refresh, ApplicationContext parent)
			throws BeansException {

		super(parent);
		setConfigLocations(configLocations);
		if (refresh) {
			refresh();
		}
	}
```

由以上构造方法可知，初始化ApplicationContext上下文，需要进行refresh的操作。`org.springframework.context.support.AbstractApplicationContext#refresh`实现如下：

```java
@Override
public void refresh() throws BeansException, IllegalStateException {
    synchronized (this.startupShutdownMonitor) {
        // Prepare this context for refreshing.
        prepareRefresh();

        // Tell the subclass to refresh the internal bean factory.
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // Prepare the bean factory for use in this context.
        prepareBeanFactory(beanFactory);

        try {
            // Allows post-processing of the bean factory in context subclasses.
            postProcessBeanFactory(beanFactory);

            // Invoke factory processors registered as beans in the context.
            invokeBeanFactoryPostProcessors(beanFactory);

            // Register bean processors that intercept bean creation.
            registerBeanPostProcessors(beanFactory);

            // Initialize message source for this context.
            initMessageSource();

            // Initialize event multicaster for this context.
            initApplicationEventMulticaster();

            // Initialize other special beans in specific context subclasses.
            onRefresh();

            // Check for listener beans and register them.
            registerListeners();

            // Instantiate all remaining (non-lazy-init) singletons.
            finishBeanFactoryInitialization(beanFactory);

            // Last step: publish corresponding event.
            finishRefresh();
        }

        catch (BeansException ex) {
            if (logger.isWarnEnabled()) {
                logger.warn("Exception encountered during context initialization - " +
                            "cancelling refresh attempt: " + ex);
            }

            // Destroy already created singletons to avoid dangling resources.
            destroyBeans();

            // Reset 'active' flag.
            cancelRefresh(ex);

            // Propagate exception to caller.
            throw ex;
        }

        finally {
            // Reset common introspection caches in Spring's core, since we
            // might not ever need metadata for singleton beans anymore...
            resetCommonCaches();
        }
    }
}
```

由此可知，首先需要获得BeanFactory实例对象，由`org.springframework.context.support.AbstractApplicationContext#obtainFreshBeanFactory`方法实现。

```java
/**
	 * Tell the subclass to refresh the internal bean factory.
	 * @return the fresh BeanFactory instance
	 * @see #refreshBeanFactory()
	 * @see #getBeanFactory()
	 */
protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
    refreshBeanFactory();
    ConfigurableListableBeanFactory beanFactory = getBeanFactory();
    if (logger.isDebugEnabled()) {
        logger.debug("Bean factory for " + getDisplayName() + ": " + beanFactory);
    }
    return beanFactory;
}
```

由以上代码可知，进行 `org.springframework.context.support.AbstractRefreshableApplicationContext#refreshBeanFactory`操作之后，就可以通过 `org.springframework.context.support.AbstractRefreshableApplicationContext#getBeanFactory`方法得到BeanFactory实例对象了。

那么，上述`refreshBeanFactory`方法是如何实现的呢？继续跟进代码，

```java
/**
	 * This implementation performs an actual refresh of this context's underlying
	 * bean factory, shutting down the previous bean factory (if any) and
	 * initializing a fresh bean factory for the next phase of the context's lifecycle.
	 */
	@Override
	protected final void refreshBeanFactory() throws BeansException {
		if (hasBeanFactory()) {
			destroyBeans();
			closeBeanFactory();
		}
		try {
			DefaultListableBeanFactory beanFactory = createBeanFactory();
			beanFactory.setSerializationId(getId());
			customizeBeanFactory(beanFactory);
			loadBeanDefinitions(beanFactory);
			synchronized (this.beanFactoryMonitor) {
				this.beanFactory = beanFactory;
			}
		}
		catch (IOException ex) {
			throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
		}
	}
```

上面核心代码就4行代码，由 `DefaultListableBeanFactory beanFactory = createBeanFactory();`可知，通过context.xml初始化的BeanFactory对象默认为 ``DefaultListableBeanFactory `。

由 `beanFactory.setSerializationId(getId());`为BeanFactory设置序列化id。

由 `customizeBeanFactory(beanFactory);`为BeanFactory·设置一些定制化配置。

由 `loadBeanDefinitions(beanFactory);` 为BeanFactory加载BeanDefinition相关的定义信息，这步是关键，为后面 `org.springframework.context.support.AbstractApplicationContext#getBean(java.lang.String, java.lang.Class<T>)` 获取Bean实例起到支撑的作用。那么，loadBeanDefinitions方法是如何实现的呢？它是由 `org.springframework.context.support.AbstractXmlApplicationContext#loadBeanDefinitions(org.springframework.beans.factory.support.DefaultListableBeanFactory)` 实现，具体代码如下：

```java
@Override
	protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
		// Create a new XmlBeanDefinitionReader for the given BeanFactory.
		XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

		// Configure the bean definition reader with this context's
		// resource loading environment.
		beanDefinitionReader.setEnvironment(this.getEnvironment());
		beanDefinitionReader.setResourceLoader(this);
		beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));

		// Allow a subclass to provide custom initialization of the reader,
		// then proceed with actually loading the bean definitions.
		initBeanDefinitionReader(beanDefinitionReader);
		loadBeanDefinitions(beanDefinitionReader);
	}
```

由上面代码可知，它交由XmlBeanDefinitionReader对象进行处理，由 `initBeanDefinitionReader(beanDefinitionReader);`进行一些初始化操作，重点是 `loadBeanDefinitions(beanDefinitionReader);`方法，定义如下：

```java
protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
		Resource[] configResources = getConfigResources();
		if (configResources != null) {
			reader.loadBeanDefinitions(configResources);
		}
		String[] configLocations = getConfigLocations();
		if (configLocations != null) {
			reader.loadBeanDefinitions(configLocations);
		}
	}
```

进行跟进，我们最终会进入到以下方法，

```java
	@Override
	public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
		return loadBeanDefinitions(new EncodedResource(resource));
	}
	public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
		....
				return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
		....	
	}
```

终于见到根据XML文件解析BeanDefinition信息了，由以上代码可知，重点是 `org.springframework.beans.factory.xml.XmlBeanDefinitionReader#doLoadBeanDefinitions`方法，继续跟进，`org.springframework.beans.factory.xml.XmlBeanDefinitionReader#registerBeanDefinitions` ，定义如下：

```java
public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
		BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
		int countBefore = getRegistry().getBeanDefinitionCount();
		documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
		return getRegistry().getBeanDefinitionCount() - countBefore;
	}
```

上述registerBeanDefinitions方法由DefaultBeanDefinitionDocumentReader实现，

```java
@Override
	public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
		this.readerContext = readerContext;
		logger.debug("Loading bean definitions");
		Element root = doc.getDocumentElement();
		doRegisterBeanDefinitions(root);
	}
```

最终的最终，注册BeanDefinitions的方法由 `org.springframework.beans.factory.xml.DefaultBeanDefinitionDocumentReader#doRegisterBeanDefinitions`完成。实现如下：

```java
protected void doRegisterBeanDefinitions(Element root) {
		BeanDefinitionParserDelegate parent = this.delegate;
		this.delegate = createDelegate(getReaderContext(), root, parent);

		.....

		preProcessXml(root);
		parseBeanDefinitions(root, this.delegate);
		postProcessXml(root);

		this.delegate = parent;
	}
```

由上述代码可知，它委托给BeanDefinitionParserDelegate类进行相应的实现。核心方法为： `parseBeanDefinitions(root, this.delegate);` 由此跟进，最终会进入到 `org.springframework.beans.factory.xml.BeanDefinitionParserDelegate#parseCustomElement(org.w3c.dom.Element, org.springframework.beans.factory.config.BeanDefinition)` 方法，实现如下：

```java
public BeanDefinition parseCustomElement(Element ele, BeanDefinition containingBd) {
		String namespaceUri = getNamespaceURI(ele);
		NamespaceHandler handler = this.readerContext.getNamespaceHandlerResolver().resolve(namespaceUri);
		if (handler == null) {
			error("Unable to locate Spring NamespaceHandler for XML schema namespace [" + namespaceUri + "]", ele);
			return null;
		}
		return handler.parse(ele, new ParserContext(this.readerContext, this, containingBd));
	}
```

所以到后面，所有BeanDefinition的解析都交给了NamespaceHandler，进而由 `org.springframework.beans.factory.xml.NamespaceHandlerSupport#parse`实现，或者由NamespaceHandlerSupport进行相应的自定义扩展实现。（见第三节）

```java
/**
	 * Parses the supplied {@link Element} by delegating to the {@link BeanDefinitionParser} that is
	 * registered for that {@link Element}.
	 */
	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		return findParserForElement(element, parserContext).parse(element, parserContext);
	}

	/**
	 * Locates the {@link BeanDefinitionParser} from the register implementations using
	 * the local name of the supplied {@link Element}.
	 */
	private BeanDefinitionParser findParserForElement(Element element, ParserContext parserContext) {
		String localName = parserContext.getDelegate().getLocalName(element);
		BeanDefinitionParser parser = this.parsers.get(localName);
		if (parser == null) {
			parserContext.getReaderContext().fatal(
					"Cannot locate BeanDefinitionParser for element [" + localName + "]", element);
		}
		return parser;
	}
```

### 1.3 Spring XML配置扩展总结

经过上述源码分析，其实只要抓住几个主要的关键类即可，他们的类层次关系如下：

ApplicationContext

- AbstractApplicationContext
  - AbstractXmlApplicationContext
    - ClassPathXmlApplicationContext



由ClassPathXmlApplicationContext的方法obtainFreshBeanFactory() 找到refresh()方法，进而找到AbstractXmlApplicationContext，然后交由XmlBeanDefinitionReader中的BeanDefinitionParserDelegate委托处理，在这个委托类中，交由NamespaceHandler注册BeanDefinitionParser，即 `org.springframework.beans.factory.xml.NamespaceHandlerSupport#registerBeanDefinitionParser`，最后，需要执行 `org.springframework.beans.factory.xml.NamespaceHandlerSupport#parse`方法即可得到BeanDefinition对象。

## 2. Spring Framework内建实现

### 2.1 Spring版本对应处理方式

Spring版本对应相应的特性，Spring模块对应相应的功能

- Spring2.0以下，利用的是DTD方式（约束较小）
  - log4j.dtd
  - spring.dtd

- Spring2.0以上，利用的是Schema方式（约束较大）
  - Spring-beans.xsd
  - Spring-context.xsd

Schema -> Java API XML Binding（JAXB）技术

SOAP(Simple Object Access protocol) -> WSDL

### 2.2 Spring 配置placeholder的两种方式

#### 2.2.1 利用bean标签配置

```xml
<bean class="org.springframework.beans.factory.config.PropertyPlaceholderConfigurer" >
        <property name="locations">
            <value>application.properties</value>
        </property>
        <property name="fileEncoding" value="UTF-8" />
    </bean>
```

#### 2.2.2 利用<context:property-placeholder> 标签

```xml
<context:property-placeholder location="classpath:application.properties" file-encoding="UTF-8" />
```

#### 2.2.3 两种方式优劣性

经过查看源码得知，上面两种方式的父类均为 `org.springframework.beans.factory.config.PlaceholderConfigurerSupport`，他们的子类分别为：

- `org.springframework.beans.factory.config.PropertyPlaceholderConfigurer`
- `org.springframework.context.support.PropertySourcesPlaceholderConfigurer`

`PropertySourcesPlaceholderConfigurer`是在spring-context.xsd文件中查到，如下所示：

```x&#39;s
<xsd:appinfo>
    <tool:annotation>
    <tool:exports 		type="org.springframework.context.support.PropertySourcesPlaceholderConfigurer"/>
    </tool:annotation>
</xsd:appinfo>
```

通过XML Schema的扩展可以替代Bean配置，它有以下优点：

- 配置简化
- 不需要配置bean中对应的class类名（不会检查类名是否正确，而XML schema是强检查的，属性类型未填就会报错）

### 2.3 Schema配置

**约定配置文件位于META-INF/spring.schemas**，存储内容格式：key=绝对路径，value=相对路径

Properties文件格式：key=value,key:value

如下所示：

```
http\://www.springframework.org/schema/context/spring-context.xsd=org/springframework/context/config/spring-context-4.3.xsd
```

注意：上面\表示转义，因为在properties文件中，Properties文件格式有两种表达方式：key=value,key:value。

Schema 绝对路径：

```
http://www.springframework.org/schema/context/spring-context.xsd
```

Schema 相对路径：

```
org/springframework/context/config/spring-context.xsd
```

然后在spring-context.xsd中定义如下命名空间（利用Schema的相对路径）：

```
targetNamespace="http://www.springframework.org/schema/context"
```

### 2.4 Namespace Handler配置

问题：schema 定义namespace -> 处理类是谁？

**约定配置文件位于META-INF/spring.handlers**，存储内容格式：key=Schema 命名空间，value=Handler类

如下所示：

```
http\://www.springframework.org/schema/context=org.springframework.context.config.ContextNamespaceHandler
```

key值就是Schema的命名空间：

```
http://www.springframework.org/schema/context
```

对应的Handler类：

```
org.springframework.context.config.ContextNamespaceHandler
```

### 2.5 Bean解析

通过查看如下`org.springframework.context.config.ContextNamespaceHandler`源码可知，spring-context.xml文件中 `<context:property-placeholder location="classpath:application.properties" file-encoding="UTF-8" />` 的Locale Element name 映射 `BeanDefinitionParser`，比如"property-placeholder" 映射`PropertyPlaceholderBeanDefinitionParser`。

```java
public class ContextNamespaceHandler extends NamespaceHandlerSupport {
	@Override
	public void init() {
		registerBeanDefinitionParser("property-placeholder", new PropertyPlaceholderBeanDefinitionParser());
		.....
	}
}
```

由此可知，我们Bean解析相关涉及两个概念：

- Bean定义：`org.springframework.beans.factory.config.BeanDefinition`
- Bean定义解析器：`org.springframework.beans.factory.xml.BeanDefinitionParser`

综上所述，Spring利用XML Schema自定义元素，然后解析Bean的定义，但是有一个缺点，就是spring-context.xml文件中的local-element-name与spring-context.xsd中的属性脱钩了，属于硬编码了（如“property-placeholder”）。

## 3. 自定义XML配置扩展

本节以一个案例说明自定义XML配置扩展。

### 3.1 定义Schema

定义Schema文件ehi.xsd,

- 定义元素 user -> `User`
- 定义 targetNamespace = http://github.com/landy8530/schema/ehi

### 3.2 建立META-INF/spring.schemas

- 建立 Schema 绝对路径和相对路径映射 - `META-INF/spring.schemas`
  - key-value格式为：绝对的 XSD = 相对的  XSD
  - `http\://github.com/landy8530/schema/ehi.xsd = ./ehi.xsd`

### 3.3 添加 ehi.xsd 到 context.xml

- 引入 namespace 

  ```
  xmlns:ehi="http://github.com/landy8530/schema/ehi"
  ```

- 配置 namesapce Schema 路径

  ```
  xsi:schemaLocation="
   http://github.com/landy8530/schema/ehi
   http://github.com/landy8530/schema/ehi.xsd"
  ```

- 引入 <`ehi:user` />

### 3.4 建立META-INF/spring.handlers

建立 Schema namespace 与 Handler 映射 - `META-INF/spring.handlers`

- 实现 `NamespaceHandledr` 或 扩展 `NamespaceHandlerSupport` - `EHINamespaceHandler`

```java
public class EHINamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("user",new UserBeanDefinitionParser());
    }
}
```

- 实现 `BeanDefinitionParser` 接口，创建 "user" 元素的 `BeanDefintionParser` 实现

```java
public class UserBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        // XML 配置: <ehi:user id="1" name="${name}" />
        // 原始 String 类型 -> Long
        String id = element.getAttribute("id");
        // String 类型 -> Placeholder ${name}
        String name = element.getAttribute("name");
        String beanName = element.getAttribute("bean-name");

        // BeanDefinition -> BeanDefinitionBuilder
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(User.class);
        // 添加 Property 值，来自于 XML 配置
        builder.addPropertyValue("id", id);
        builder.addPropertyValue("name", name);
        // 创建 BeanDefinition
        BeanDefinition beanDefinition =  builder.getBeanDefinition();

        // 从 parserContext 获取 Bean 注册器
        BeanDefinitionRegistry beanDefinitionRegistry = parserContext.getRegistry();
        // 注册 Bean 定义
        beanDefinitionRegistry.registerBeanDefinition(beanName,beanDefinition);

        return beanDefinition;
    }
}
```

- 建立 namespace 与 `BeanDefintionParser` 映射

  - `http\://github.com/landy8530/schema/ehi = org.landy.springxmlextension.context.config.EHINamespaceHandler`


