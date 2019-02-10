package org.landy.javai18ndemo;


import java.net.URL;

/**
 *
 *
 *
 */
public class ClassLoaderDemo {

    public static void main(String[] args) {
        //bootstrap classloader －引导（也称为原始）类加载器，它负责加载Java的核心类。
        //在Sun的JVM中，在执行java的命令中使用-Xbootclasspath选项或使用 - D选项指定sun.boot.class.path系统属性值可以指定附加的类。
        //这个加载器的是非常特殊的，它实际上不是 java.lang.ClassLoader的子类，而是由JVM自身实现的。
        //获得bootstrap classloader加载了那些核心类库：
        URL[] urls=sun.misc.Launcher.getBootstrapClassPath().getURLs();
        for (int i = 0; i < urls.length; i++) {
            System.out.println(urls[i].toExternalForm());
        }

        //extension classloader －扩展类加载器，它负责加载JRE的扩展目录（JAVA_HOME/jre/lib/ext或者由java.ext.dirs系统属性指定的）中JAR的类包。
        // 这为引入除Java核心类以外的新功能提供了一个标准机制。
        // 因为默认的扩展目录对所有从同一个JRE中启动的JVM都是通用的，所以放入这个目录的 JAR类包对所有的JVM和system classloader都是可见的。
        // 在这个实例上调用方法getParent()总是返回空值null，因为引导加载器bootstrap classloader不是一个真正的ClassLoader实例
        System.out.println(System.getProperty("java.ext.dirs"));
        ClassLoader extensionClassloader=ClassLoader.getSystemClassLoader().getParent();
        System.out.println("the parent of extension classloader : "+extensionClassloader.getParent());

        //system classloader －系统（也称为应用）类加载器，它负责在JVM被启动时，
        // 加载来自在命令java中的-classpath或者java.class.path系统属性或者CLASSPATH 作系统属性所指定的JAR类包和类路径。
        // 总能通过静态方法ClassLoader.getSystemClassLoader()找到该类加载器。
        // 如果没有特别指定，则用户自定义的任何类加载器都将该类加载器作为它的父加载器。执行以下代码即可获得：
        System.out.println(System.getProperty("java.class.path"));

        //classloader 加载类用的是全盘负责委托机制。所谓全盘负责，即是当一个classloader加载一个Class的时候，这个Class所依赖的和引用的所有 Class也由这个classloader负责载入，除非是显式的使用另外一个classloader载入；委托机制则是先让parent（父）类加载器 (而不是super，它与parent classloader类不是继承关系)寻找，只有在parent找不到的时候才从自己的类路径中去寻找。
        // 此外类加载还采用了cache机制，也就是如果 cache中保存了这个Class就直接返回它，如果没有才从文件中读取和转换成Class，并存入cache，这就是为什么我们修改了Class但是必须重新启动JVM才能生效的原因。

        //类加载器的顺序是：
        //先是bootstrap classloader，然后是extension classloader，最后才是system classloader。

        // 将会看到结果是null，这就表明java.lang.System是由bootstrap classloader加载的，
        // 因为bootstrap classloader不是一个真正的ClassLoader实例，而是由JVM实现的，正如前面已经说过的。
        System.out.println(System.class.getClassLoader());

        // extension classloader实际上是sun.misc.LauncherExtClassLoader类的一个实例，
        // system classloader实际上是sun.misc.LauncherAppClassLoader类的一个实例。并且都是 java.net.URLClassLoader的子类。
    }

}
