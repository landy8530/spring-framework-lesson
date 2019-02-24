# Table of Contents

* [Java Beans内省机制以及在Spring中的应用](#java-beans内省机制以及在spring中的应用)
  * [1. 企业级Beans](#1-企业级beans)
  * [2. Java Beans内省机制](#2-java-beans内省机制)
    * [2.1 Bean基础](#21-bean基础)
    * [2.2 Java反射](#22-java反射)
    * [2.3 内省](#23-内省)
      * [2.3.1 概念](#231-概念)
      * [2.3.2 BeanDescriptor](#232-beandescriptor)
      * [2.3.3 MethodDescriptor](#233-methoddescriptor)
      * [2.3.4 PropertyDescriptor](#234-propertydescriptor)
  * [3. Java Beans事件监听](#3-java-beans事件监听)
    * [3.1 属性变化概念](#31-属性变化概念)
      * [3.1.1 属性变化监听器(PropertyChangeListener)](#311-属性变化监听器(PropertyChangeListener))
      * [3.1.2 属性变化事件(PropertyChangeEvent)](#312-属性变化事件(PropertyChangeEvent))
    * [3.2 属性事件监听源码解析](#32-属性事件监听源码解析)
  * [4. Spring Beans属性处理](#4-spring-beans属性处理)

# Java Beans内省机制以及在Spring中的应用

## 1. 企业级Beans

EJB就像共产主义，听说过，没见过

EJB3.1以后是嵌入式的

- RPC
  - 有状态Bean和无状态Bean
- JPA
  - 持久化Bean
- JMS
  - 消息Bean

## 2. Java Beans内省机制

### 2.1 Bean基础

- 充血模型和贫血模型
  - 充血模型：包括了一些状态和具体的业务操作
  - 贫血模型：只有setter和getter
- 状态Bean
  - Session Bean
  - JSF：除了页面上的操作，还需要管理Session（Session Bean）
  - 状态Bean：比如有个远程服务

### 2.2 Java反射

获取Class信息

- 构造器（Constructor）
- 方法（Method）
- 字段（Field）

### 2.3 内省

#### 2.3.1 概念

获取Java BeanInfo，Bean的描述针对的是某个类的

类（模版）没有状态，因为类定义好了以后就是字节码，固定了，而实例（Bean）是有状态的。

- Bean描述符（BeanDescriptor）：不只是Bean的描述，
- 方法描述符（MethodDescriptor）
- 字段描述符（FieldDecriptor）

#### 2.3.2 BeanDescriptor

```java
public class BeanDescriptor extends FeatureDescriptor {

    private Reference<? extends Class<?>> beanClassRef;
    private Reference<? extends Class<?>> customizerClassRef;
}    
```

#### 2.3.3 MethodDescriptor

Java MethodDescriptor实现：

```java
public class MethodDescriptor extends FeatureDescriptor {

    private final MethodRef methodRef = new MethodRef();
    ....
}    
```

```java
final class MethodRef {
    private String signature;
    private SoftReference<Method> methodRef;
    private WeakReference<Class<?>> typeRef;
    .....
}        
```

上述代码说明：

BeanInfo 对象不是单例，是多例。

Class很多时候是单例的。

弱引用（WeakReference）：常当作缓存，不用的时候可以马上让GC收回（其中保存的对象实例可以被GC回收掉）。Class实例暂存于一个由WeakReference构成的Map中作为Cache。

软引用（SoftReference）：强引用，它保存的对象实例，除非JVM即将OutOfMemory，否则不会被GC回收。这个特性使得它特别适合设计对象Cache。对于Cache，我们希望被缓存的对象最好始终常驻内存，但是如果JVM内存吃紧，为了不发生OutOfMemoryError导致系统崩溃，必要的时候也允许JVM回收Cache的内存，待后续合适的时机再把数据重新Load到Cache中。这样可以系统设计得更具弹性。



setter和getter并不是需要成对出现

#### 2.3.4 PropertyDescriptor

Java PropertyDescriptor实现

```java
public class PropertyDescriptor extends FeatureDescriptor {

    private Reference<? extends Class<?>> propertyTypeRef;
    private final MethodRef readMethodRef = new MethodRef();
    private final MethodRef writeMethodRef = new MethodRef();
    private Reference<? extends Class<?>> propertyEditorClassRef;
    .....
}        
```

readMethodRef和writeMethodRef可以任意为空，但是不会同时为空，因为判断一个property的时候，我们必须根据setter和getter来判断。

## 3. Java Beans事件监听

### 3.1 属性变化概念

#### 3.1.1 属性变化监听器(PropertyChangeListener)

- 传递了一个字符串（Text）类型
- 传递值转化成对应的数据类型，并且赋值
- 对应事件发生（不同属性对应的事件处理可能不同）

#### 3.1.2 属性变化事件(PropertyChangeEvent)

- 事件源（Source）
- 属性名称（PropertyName）
- 变化前值（OldValue）
- 变化后值（NewValue）

### 3.2 属性事件监听源码解析

对每个对象的属性进行设值的过程需要自定义Editor类，继承自

```java
java.beans.PropertyEditorSupport
```

在Spring中，有很多已定义的各种Editor，比如CharsetEditor等，比如：

```java
public class CharsetEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (StringUtils.hasText(text)) {
			setValue(Charset.forName(text));
		}
		else {
			setValue(null);
		}
	}
	....
}    
```

通过以上代码可知，他统一把传入的文本值text通过setValue方法转化为相应类型的值了。setValue实现如下：

```
java.beans.PropertyEditorSupport#setValue
```

```java
/**
     * Set (or change) the object that is to be edited.
     *
     * @param value The new target object to be edited.  Note that this
     *     object should not be modified by the PropertyEditor, rather
     *     the PropertyEditor should create a new object to hold any
     *     modified value.
     */
    public void setValue(Object value) {
        this.value = value;
        firePropertyChange();
    }
```

由上可知，它不仅是自己进行了设值操作，并且触发了一个方法

```java
java.beans.PropertyEditorSupport#firePropertyChange
```

实现如下：

```java
/**
  * Report that we have been modified to any interested listeners.
  */
public void firePropertyChange() {
    java.util.Vector<PropertyChangeListener> targets;
    synchronized (this) {
        if (listeners == null) {
            return;
        }
        targets = unsafeClone(listeners);
    }
    // Tell our listeners that "everything" has changed.
    PropertyChangeEvent evt = new PropertyChangeEvent(source, null, null, null);

    for (int i = 0; i < targets.size(); i++) {
        PropertyChangeListener target = targets.elementAt(i);
        target.propertyChange(evt);
    }
}
```

说明我们需要设置属性值的时候，不仅需要定义PropertyEditorSupport的子类，然后通过java.beans.PropertyDescriptor#setPropertyEditorClass方法设值，还需要定义事件监听器，实现PropertyChangeListener，通过事件回调的方式实现属性值的设置。实现如下：

```java
private static class SetPropertyChangeListener implements PropertyChangeListener {

        private final Object bean;
        private final Method setterMethod;

        private SetPropertyChangeListener(Object bean, Method setterMethod) {
            this.setterMethod = setterMethod;
            this.bean = bean;
        }

        @Override
        public void propertyChange(PropertyChangeEvent event) {
            //事件源
            PropertyEditor source = (PropertyEditor) event.getSource();
            try {
                setterMethod.invoke(bean, source.getValue());
            } catch (Exception e) {
            }
        }
    }
```

以上代码中的事件源，根据java.beans.PropertyEditorSupport的构造方法得知，事件源对象就是实现PropertyEditorSupport的子类对象。所以source.getValue()对应的值就是org.springframework.beans.propertyeditors.CharsetEditor#setAsText方法参数text对应的值了。这样就全部串起来了。

```java
/**
     * Constructs a <code>PropertyEditorSupport</code> object.
     *
     * @since 1.5
     */
public PropertyEditorSupport() {
    setSource(this);
}
```



## 4. Spring Beans属性处理

- 属性修改器（PropertyEditor）
- 属性修改器注册（PropertyEditorRegistry）
- PropertyEditor注册器（PropertyEditorRegistrar）

```java
public class MyPropertyEditorRegistrar implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        // 将Date 类型 date 字段设置 PropertyEditor
        registry.registerCustomEditor(Date.class,"date",new DatePropertyEditor());
    }
}
```

- 自定义PropertyEditor配置器（CustomEditorConfigurer）

```xml
<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
    <property name="propertyEditorRegistrars">
        <list>
            <bean class="org.landy.springjavabeans.spring.MyPropertyEditorRegistrar" />
        </list>
    </property>
</bean>
```