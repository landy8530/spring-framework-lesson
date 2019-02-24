# Spring Framework

## 多JDK版本说明

以前安装JDK，需要手动配置环境变量。JDK8以后多了自动配置环境变量，所以可以不用手动配置。如果我已经装了JDK8，还想再装一个JDK9，安装完，自动配置的环境变量会指向JDK9版本。

### 解决方法

#### 1. 删除自动配置的环境变量 

自动配置的环境变量是一个隐藏目录：`C:\ProgramData\Oracle\Java\javapath`，删掉这个目录下的3个exe文件，系统就无法匹配到了。 

#### 2. 手动配置环境变量 

自动匹配是匹配不到了，所以我们用老办法，手动配置环境变量即可。这样，我们可以根据环境变量里配置的JDK版本去实现版本切换了。

## 1. Aware接口

### 1.1 接口定义

定义如下：

```java
/**
 * A marker superinterface indicating that a bean is eligible to be notified by the
 * Spring container of a particular framework object through a callback-style method.
 * The actual method signature is determined by individual subinterfaces but should
 * typically consist of just one void-returning method that accepts a single argument.
 *
 * <p>Note that merely implementing {@link Aware} provides no default functionality.
 * Rather, processing must be done explicitly, for example in a
 * {@link org.springframework.beans.factory.config.BeanPostProcessor}.
 * Refer to {@link org.springframework.context.support.ApplicationContextAwareProcessor}
 * for an example of processing specific {@code *Aware} interface callbacks.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.1
 */
public interface Aware {

}
```

通过如上定义可以知道，这是一个标记接口，继承自这个接口的Bean都可以被通知，通过回调方式方法的特定对象的Spring容器。实际的方法签名由其子接口决定。

### 1.2 接口处理器

所有Aware子接口的处理器由 `org.springframework.context.support.ApplicationContextAwareProcessor` 处理。定义如下：

```java
/**
 * {@link org.springframework.beans.factory.config.BeanPostProcessor}
 * implementation that passes the ApplicationContext to beans that
 * implement the {@link EnvironmentAware}, {@link EmbeddedValueResolverAware},
 * {@link ResourceLoaderAware}, {@link ApplicationEventPublisherAware},
 * {@link MessageSourceAware} and/or {@link ApplicationContextAware} interfaces.
 *
 * <p>Implemented interfaces are satisfied in order of their mention above.
 *
 * <p>Application contexts will automatically register this with their
 * underlying bean factory. Applications do not use this directly.
 *
 * @author Juergen Hoeller
 * @author Costin Leau
 * @author Chris Beams
 * @since 10.10.2003
 * @see org.springframework.context.EnvironmentAware
 * @see org.springframework.context.EmbeddedValueResolverAware
 * @see org.springframework.context.ResourceLoaderAware
 * @see org.springframework.context.ApplicationEventPublisherAware
 * @see org.springframework.context.MessageSourceAware
 * @see org.springframework.context.ApplicationContextAware
 * @see org.springframework.context.support.AbstractApplicationContext#refresh()
 */
class ApplicationContextAwareProcessor implements BeanPostProcessor {

	private final ConfigurableApplicationContext applicationContext;

	private final StringValueResolver embeddedValueResolver;

	.....

}
```

由以上代码可知，目前主要的Aware子接口有如下几个：

- `org.springframework.context.EnvironmentAware`
- `org.springframework.context.EmbeddedValueResolverAware`
- `org.springframework.context.ResourceLoaderAware`
- `org.springframework.context.ApplicationEventPublisherAware`
- `org.springframework.context.MessageSourceAware`
- `org.springframework.context.ApplicationContextAware`

通过这个类，Spring容器中的Application contexts会自动注册相关的Bean Factory对象，实现如下：

```java
@Override
public Object postProcessBeforeInitialization(final Object bean, String beanName) throws BeansException {
    AccessControlContext acc = null;

    if (System.getSecurityManager() != null &&
        (bean instanceof EnvironmentAware || bean instanceof EmbeddedValueResolverAware ||
         bean instanceof ResourceLoaderAware || bean instanceof ApplicationEventPublisherAware ||
         bean instanceof MessageSourceAware || bean instanceof ApplicationContextAware)) {
        acc = this.applicationContext.getBeanFactory().getAccessControlContext();
    }

    if (acc != null) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                invokeAwareInterfaces(bean);
                return null;
            }
        }, acc);
    }
    else {
        invokeAwareInterfaces(bean);
    }

    return bean;
}

private void invokeAwareInterfaces(Object bean) {
    if (bean instanceof Aware) {
        if (bean instanceof EnvironmentAware) {
            ((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
        }
        if (bean instanceof EmbeddedValueResolverAware) {
            ((EmbeddedValueResolverAware) bean).setEmbeddedValueResolver(this.embeddedValueResolver);
        }
        if (bean instanceof ResourceLoaderAware) {
            ((ResourceLoaderAware) bean).setResourceLoader(this.applicationContext);
        }
        if (bean instanceof ApplicationEventPublisherAware) {
            ((ApplicationEventPublisherAware) bean).setApplicationEventPublisher(this.applicationContext);
        }
        if (bean instanceof MessageSourceAware) {
            ((MessageSourceAware) bean).setMessageSource(this.applicationContext);
        }
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
        }
    }
}
```

### 1.3 处理器如何被调用？

#### 1.3.1 处理器初始化

通过 `org.springframework.context.support.ApplicationContextAwareProcessor`处理器类的注释可以看到一个方法，`org.springframework.context.support.AbstractApplicationContext#refresh()`，我们可以猜测这个方法就是调用处理器的入口方法。refresh方法定义如下：

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

			......
		}
	}
```

在prepareBeanFactory方法中，我们找到了该处理器的初始化。

```java
/**
	 * Configure the factory's standard context characteristics,
	 * such as the context's ClassLoader and post-processors.
	 * @param beanFactory the BeanFactory to configure
	 */
	protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		// Tell the internal bean factory to use the context's class loader etc.
		beanFactory.setBeanClassLoader(getClassLoader());
		beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
		beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

		// Configure the bean factory with context callbacks.
		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
		//设置可以忽略的依赖接口，其实就是由ApplicationContextAwareProcessor处理器处理了
        beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
		beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
		beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
		beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
		
        ......
		
	}
```

#### 1.3.2 处理器调用

通过查看 `org.springframework.context.support.ApplicationContextAwareProcessor#postProcessBeforeInitialization` 可以知道，处理器被 `org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization` 方法调用。

```java
@Override
public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
    throws BeansException {

    Object result = existingBean;
    for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
        result = beanProcessor.postProcessBeforeInitialization(result, beanName);
        if (result == null) {
            return result;
        }
    }
    return result;
}
```

以上方法又被 `org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#initializeBean(java.lang.String, java.lang.Object, org.springframework.beans.factory.support.RootBeanDefinition)`方法调用，定义如下：

```java
protected Object initializeBean(final String beanName, final Object bean, RootBeanDefinition mbd) {
		....

		Object wrappedBean = bean;
		if (mbd == null || !mbd.isSynthetic()) {
			wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
		}

		.....
		return wrappedBean;
	}
```

该方法被 `org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#configureBean`方法调用，定义如下：

```java
@Override
public Object configureBean(Object existingBean, String beanName) throws BeansException {
   markBeanAsCreated(beanName);
   BeanDefinition mbd = getMergedBeanDefinition(beanName);
   RootBeanDefinition bd = null;
   if (mbd instanceof RootBeanDefinition) {
      RootBeanDefinition rbd = (RootBeanDefinition) mbd;
      bd = (rbd.isPrototype() ? rbd : rbd.cloneBeanDefinition());
   }
   if (!mbd.isPrototype()) {
      if (bd == null) {
         bd = new RootBeanDefinition(mbd);
      }
      bd.setScope(BeanDefinition.SCOPE_PROTOTYPE);
      bd.allowCaching = ClassUtils.isCacheSafe(ClassUtils.getUserClass(existingBean), getBeanClassLoader());
   }
   BeanWrapper bw = new BeanWrapperImpl(existingBean);
   initBeanWrapper(bw);
   populateBean(beanName, bd, bw);
   return initializeBean(beanName, existingBean, bd);
}
```

随后，该方法会被 `org.springframework.beans.factory.wiring.BeanConfigurerSupport#configureBean`方法调用，定义如下：

```java
/**
	 * Configure the bean instance.
	 * <p>Subclasses can override this to provide custom configuration logic.
	 * Typically called by an aspect, for all bean instances matched by a pointcut.
	 * @param beanInstance the bean instance to configure (must <b>not</b> be {@code null})
	 */
	public void configureBean(Object beanInstance) {
		if (this.beanFactory == null) {
			if (logger.isDebugEnabled()) {
				logger.debug("BeanFactory has not been set on " + ClassUtils.getShortName(getClass()) + ": " +
						"Make sure this configurer runs in a Spring container. Unable to configure bean of type [" +
						ClassUtils.getDescriptiveType(beanInstance) + "]. Proceeding without injection.");
			}
			return;
		}

		BeanWiringInfo bwi = this.beanWiringInfoResolver.resolveWiringInfo(beanInstance);
		if (bwi == null) {
			// Skip the bean if no wiring info given.
			return;
		}

		try {
			if (bwi.indicatesAutowiring() ||
					(bwi.isDefaultBeanName() && !this.beanFactory.containsBean(bwi.getBeanName()))) {
				// Perform autowiring (also applying standard factory / post-processor callbacks).
				this.beanFactory.autowireBeanProperties(beanInstance, bwi.getAutowireMode(), bwi.getDependencyCheck());
				Object result = this.beanFactory.initializeBean(beanInstance, bwi.getBeanName());
				checkExposedObject(result, beanInstance);
			}
			else {
				// Perform explicit wiring based on the specified bean definition.
				Object result = this.beanFactory.configureBean(beanInstance, bwi.getBeanName());
				checkExposedObject(result, beanInstance);
			}
		}
		catch (BeanCreationException ex) {
			......
		}
	}
```

以上configureBean方法可以被子类重写，通常由aspect调用，用于匹配pointcut的所有实例。

根据BeanConfigurerSupport的定义，可知，真正调用configureBean方法的beanFactory是ConfigurableListableBeanFactory接口，其默认实现为： `org.springframework.beans.factory.support.DefaultListableBeanFactory` 

```java
public class BeanConfigurerSupport implements BeanFactoryAware, InitializingBean, DisposableBean {

	/** Logger available to subclasses */
	protected final Log logger = LogFactory.getLog(getClass());

	private volatile BeanWiringInfoResolver beanWiringInfoResolver;

	private volatile ConfigurableListableBeanFactory beanFactory;
	.....
｝	
```

