# Table of Contents

* [Java资源管理以及在Spring中的应用](#java资源管理以及在spring中的应用)
  * [1. 资源种类](#1-资源种类)
    * [1.1 文件资源](#11-文件资源)
    * [1.2 网络资源（Socket）](#12-网络资源（socket）)
    * [1.3 类路径资源](#13-类路径资源)
  * [2. Java URL 协议扩展](#2-java-url-协议扩展)
    * [2.1 URL协议扩展过程](#21-url协议扩展过程)
    * [2.2 URL协议扩展原理](#22-url协议扩展原理)
  * [3. Spring资源管理](#3-spring资源管理)
  * [4. 其他说明](#4-其他说明)

# Java资源管理以及在Spring中的应用

## 1. 资源种类

### 1.1 文件资源

- XML文件
- Properties文件

```java
File file1 = new File(""); //默认为用户路径
System.out.println(file1.getAbsolutePath());
System.out.println(System.getProperty("user.dir"));//用户当前工作路径
File file = new File("D:\\projects\\idea_workspace\\spring-boot-lesson\\spring-java-resources\\src\\main\\resources\\application.properties");
URL fileURL = file.toURI().toURL();
System.out.println(fileURL);
```

以上代码得出结果：

```
file:/D:/projects/idea_workspace/spring-boot-lesson/spring-java-resources/src/main/resources/application.properties
```

它是file协议（URL）

读取Properties文件事例

```java
public class PropertiesDemo {

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        //1. 绝对路径方式，不妥
//        File file = new File("D:\\projects\\idea_workspace\\spring-boot-lesson\\spring-java-resources\\src\\main\\resources\\application.properties");
//        properties.load(new FileReader(file));
        //2. 可以读取class path路径下的,太多，不可取
//        System.out.println(System.getProperty("java.class.path"));

        //3. 利用ClassLoader，读取class path路径下的资源（相对路径）
        //Maven默认的Resource目录就会放到class path下
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        //读取class path路径下的文件
        InputStream is = classLoader.getResourceAsStream("application.properties");
        properties.load(is);
        System.out.println(properties.getProperty("spring.application.name"));
    }
}
```



### 1.2 网络资源（Socket）

- HTTP
- FTP

利用`org.springframework.web.client.RestTemplate` 访问HTTP资源：

```java
//https://start.spring.io/
// Spring RestTemplate
RestTemplate restTemplate = new RestTemplate();

InputStream inputStreamFromRestTemplate =
    restTemplate.execute("https://start.spring.io/",
                         HttpMethod.GET,
                         request -> {
                         },
                         response -> {
                             return response.getBody();
                         }
    );

System.out.println(inputStreamFromRestTemplate);
```

利用`java.net.URL`访问HTTP资源：

```java
URL url = new URL("https://start.spring.io/");

InputStream inputStreamFromURL = url.openStream();

System.out.println(inputStreamFromURL); //inputStreamFromRestTemplate对应的实现类与inputStreamFromURL对应的实现类相同
```

### 1.3 类路径资源

## 2. Java URL 协议扩展

### 2.1 URL协议扩展过程

Java URL协议具有以下层次关系：

`java.net.URL#setURLStreamHandlerFactory` ->  `java.net.URLStreamHandlerFactory#createURLStreamHandler` -> `java.net.URLStreamHandler#openConnection(java.net.URL)` 

由上面的代码`InputStream inputStreamFromURL = url.openStream();`可得知，需要通过`java.net.URL#openStream`得到`InputStream`对象。

```java
public final InputStream openStream() throws java.io.IOException {
    return openConnection().getInputStream();
}
```

由`java.net.URL#openConnection()`方法可得知，

```java
public URLConnection openConnection() throws java.io.IOException {
    return handler.openConnection(this);
}
```

需要得到 `java.net.URLConnection` ，需要使用`java.net.URLStreamHandler#openConnection(java.net.URL)` 方法，而`java.net.URLStreamHandler`是一个抽象类，比如有以下的具体实现：

```
//策略模式的实现
//REMIND: decide whether to allow the "null" class prefix or not.
//packagePrefixList += "sun.net.www.protocol";
//模式 URLStreamHandler =  sun.net.www.protocol.${protocol}.Handler
file协议：URLStreamHandler = sun.net.www.protocol.file.Handler
http协议：URLStreamHandler = sun.net.www.protocol.http.Handler
https协议：URLStreamHandler = sun.net.www.protocol.https.Handler
jar协议：URLStreamHandler = sun.net.www.protocol.jar.Handler
ftp协议：URLStreamHandler = sun.net.www.protocol.ftp.Handler
```

通过 `java.net.URLConnection#getInputStream` 得知，需要得到具体的InputStream，则需要扩展URLConnection，比如 `sun.net.www.protocol.http.HttpURLConnection` ，它继承自 `java.net.HttpURLConnection`。

```java

    public synchronized InputStream getInputStream() throws IOException {
        this.connecting = true;
        SocketPermission var1 = this.URLtoSocketPermission(this.url);
        if(var1 != null) {
            try {
                return (InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction<InputStream>() {
                    public InputStream run() throws IOException {
                        return HttpURLConnection.this.getInputStream0();
                    }
                }, (AccessControlContext)null, new Permission[]{var1});
            } catch (PrivilegedActionException var3) {
                throw (IOException)var3.getException();
            }
        } else {
            return this.getInputStream0();
        }
    }
```

 

URL -> URLConnection -> URLStreamHandler -> InputStream

### 2.2 URL协议扩展原理

针对每个协议的不同，JDK利用策略模式进行相应的实现，每个协议都有相应的Handler。

比如URL其中一个构造方法：

```java
public URL(String protocol, String host, int port, String file,
               URLStreamHandler handler) throws MalformedURLException {
    if (handler != null) {
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            // check for permission to specify a handler
            checkSpecifyHandler(sm);
        }
    }

    ....

        // Note: we don't do validation of the URL here. Too risky to change
        // right now, but worth considering for future reference. -br
        if (handler == null &&
            (handler = getURLStreamHandler(protocol)) == null) {
            throw new MalformedURLException("unknown protocol: " + protocol);
        }
    this.handler = handler;
}
```

上面构造方法得知，每个URL都可以跟进不同的Handler构造。

需要扩展URL协议的话，需要按照约定的Handler包命名规则：

`sun.net.www.protocol（前缀） + 协议（${protocol}） + Handler`

思考一个问题：Spring Boot打成Jar包，是如何访问到Jar包中的资源？

- Spring boot其实对 `java.net.URLStreamHandlerFactory`做了扩展

  - `org.springframework.boot.context.embedded.tomcat.SslStoreProviderUrlStreamHandlerFactory`
  - `org.springframework.boot.context.embedded.jetty.JasperInitializer.WarUrlStreamHandlerFactory`

- JDK对`java.net.URLStreamHandlerFactory`的默认实现

  - `sun.misc.Launcher.Factory` 它的实现如下：

  ```java
  private static class Factory implements URLStreamHandlerFactory {
      private static String PREFIX = "sun.net.www.protocol";
  
      private Factory() {
      }
  
      public URLStreamHandler createURLStreamHandler(String var1) {
          String var2 = PREFIX + "." + var1 + ".Handler";
  
          try {
              Class var3 = Class.forName(var2);
              return (URLStreamHandler)var3.newInstance();
          } catch (ReflectiveOperationException var4) {
              throw new InternalError("could not load " + var1 + "system protocol handler", var4);
          }
      }
  }
  ```

  通过查看factory调用的源码可知，Spring Boot读取Jar文件中的资源是跟`java.net.URLClassLoader#URLClassLoader(java.net.URL[], java.lang.ClassLoader, java.net.URLStreamHandlerFactory)`有关的。

  ```java
  static class AppClassLoader extends URLClassLoader {
      	.....    
      
          AppClassLoader(URL[] var1, ClassLoader var2) {
              super(var1, var2, Launcher.factory);
          }
  
          .....
      }
  ```

  在读取Jar包中资源的时候，他已经设置了相应的URLStreamHandlerFactory（即：``java.net.URL#setURLStreamHandlerFactory`` ），进而创建相应的URLStreamHandler，即：`sun.net.www.protocol.jar.Handler`。

## 3. Spring资源管理

Spring中资源管理是Resource接口，`org.springframework.core.io.Resource`,他有以下一些实现：

- `org.springframework.core.io.FileSystemResource`
- `org.springframework.core.io.PathResource`
- `org.springframework.core.io.ClassPathResource`

通过 `org.springframework.core.io.ResourceLoader#getResource` 得到相应Resource对象，而 `org.springframework.core.io.ResourceLoade`有很多种实现，其默认实现为：`org.springframework.core.io.DefaultResourceLoader`。

如下案例说明ResourceLoader的用法：

```java
public class ResourceDemo {

    public static void main(String[] args) throws Exception {
        // Resource
        // FileSystemResource
        // ClasspathResource
        DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource("classpath:/application.properties");

        InputStream inputStream = resource.getInputStream();

        String content = StreamUtils.copyToString(inputStream, Charset.forName("UTF-8"));

        System.out.println(content);
    }
}
```

如上代码发现，需要获取InputStream，则需要通过ResourceLoader获取Resource，然后调用getInputStream得到。具体如下：

`org.springframework.core.io.DefaultResourceLoader#getResource` 方法：

```java
@Override
public Resource getResource(String location) {
    Assert.notNull(location, "Location must not be null");

    for (ProtocolResolver protocolResolver : this.protocolResolvers) {
        Resource resource = protocolResolver.resolve(location, this);
        if (resource != null) {
            return resource;
        }
    }

    if (location.startsWith("/")) {
        return getResourceByPath(location);
    }
    else if (location.startsWith(CLASSPATH_URL_PREFIX)) {
        return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()), getClassLoader());
    }
    else {
        try {
            // Try to parse the location as a URL...
            URL url = new URL(location);
            return new UrlResource(url);
        }
        catch (MalformedURLException ex) {
            // No URL -> resolve as resource path.
            return getResourceByPath(location);
        }
    }
}
```

上述代码可知，如果url前缀为“classpath:”的时候，则通过 `org.springframework.core.io.ClassPathResource#ClassPathResource(java.lang.String, java.lang.ClassLoader)` 实现，其`getInputStream`方法如下：

```java
public InputStream getInputStream() throws IOException {
    InputStream is;
    if (this.clazz != null) {
        is = this.clazz.getResourceAsStream(this.path);
    }
    else if (this.classLoader != null) {
        is = this.classLoader.getResourceAsStream(this.path);
    }
    else {
        is = ClassLoader.getSystemResourceAsStream(this.path);
    }
    if (is == null) {
        throw new FileNotFoundException(getDescription() + " cannot be opened because it does not exist");
    }
    return is;
}
```

如果不是“classpath:”开头的，则由 `org.springframework.core.io.UrlResource` 实现，此时底层`getInputStream`实现就是委派给了`java.net.URLConnection`实现。如下代码所示：

```java
/**
	 * This implementation opens an InputStream for the given URL.
	 * <p>It sets the {@code useCaches} flag to {@code false},
	 * mainly to avoid jar file locking on Windows.
	 * @see java.net.URL#openConnection()
	 * @see java.net.URLConnection#setUseCaches(boolean)
	 * @see java.net.URLConnection#getInputStream()
	 */
	@Override
	public InputStream getInputStream() throws IOException {
		URLConnection con = this.url.openConnection();
		ResourceUtils.useCachesIfNecessary(con);
		try {
			return con.getInputStream();
		}
		catch (IOException ex) {
			// Close the HTTP connection (if applicable).
			if (con instanceof HttpURLConnection) {
				((HttpURLConnection) con).disconnect();
			}
			throw ex;
		}
	}
```

综上，说明Spring底层实现也是采用Java URL扩展协议的实现，同理，`org.springframework.core.io.ClassPathResource#getURL`方法实现也是委派给Class或者ClassLoader去实现。

```java
/**
	 * Resolves a URL for the underlying class path resource.
	 * @return the resolved URL, or {@code null} if not resolvable
	 */
protected URL resolveURL() {
    if (this.clazz != null) {
        return this.clazz.getResource(this.path);
    }
    else if (this.classLoader != null) {
        return this.classLoader.getResource(this.path);
    }
    else {
        return ClassLoader.getSystemResource(this.path);
    }
}
```



## 4. 其他说明

排除某个配置文件在maven中的配置（pom.xml）

```xml
<resources>
    <resource>
        <directory>${basedir}/src/main/resources</directory>
        <!--包含resource的路径-->
        <includes>
            <!-- basedir：当前module所在的根目录 -->
            <include>${basedir}</include>
        </includes>
        <!--排除某个resource的路径-->
        <excludes>
            <exclude>
                *.properties
            </exclude>
        </excludes>
    </resource>
</resources>
```
