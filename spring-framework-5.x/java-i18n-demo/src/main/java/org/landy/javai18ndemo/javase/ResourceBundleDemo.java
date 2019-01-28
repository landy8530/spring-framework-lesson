package org.landy.javai18ndemo.javase;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.*;
import java.util.spi.ResourceBundleControlProvider;

/**
 * {@link ResourceBundle} 示例
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/28
 */
public class ResourceBundleDemo {

    /**
     * ResourceBundle 实现，查找 properties 文件中的国际化内容
     * 默认实现是采用 ISO 8859-1，所以凡是出现中文就会乱码
     * 方法一：
     *  可以采用 native2ascii 方法，将打包后的资源文件进行转移，而不是直接在源码方面解决
     * 方法二：
     *  since Java 1.6，扩展 {@link ResourceBundle.Control}
     *  缺点：可移植性不强，不得不显示地传递 {@link ResourceBundle.Control}
     * 方法三：
     *  since Java 1.8，实现 ResourceBundleControlProvider
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        // -Dfile.encoding=UTF-8 无效
        // native2ascii -> Unicode
        // package（目录） + resource 名称（不包含.properties）
        String baseName = "static.default";
        // 动态寻找相应语言的配置文件
//        ResourceBundle resourceBundleLocale = ResourceBundle.getBundle(baseName);
//        System.out.println("resourceBundle.name : " + resourceBundleLocale.getString("name"));
        // 基于 Java 1.6
        // 显示地传递 EncodedControl
//        ResourceBundle resourceBundle = ResourceBundle.getBundle(baseName,new EncodedControl());
//        System.out.println("resourceBundle.name : " + resourceBundle.getString("name"));
        // 使用默认 ResourceBundleControlProvider SPI(Service Provider Interface) 机制
        ResourceBundle resourceBundle1 = ResourceBundle.getBundle(baseName);
        System.out.println("resourceBundle.name : " + resourceBundle1.getString("name"));

        ServiceLoader<ResourceBundleControlProvider> operations = ServiceLoader.load(ResourceBundleControlProvider.class);
        Iterator<ResourceBundleControlProvider> operationIterator = operations.iterator();
        System.out.println("classPath:"+System.getProperty("java.class.path"));
        while (operationIterator.hasNext()) {
            ResourceBundleControlProvider operation = operationIterator.next();
            System.out.println(operation);
            System.out.println(operation.getControl(baseName));
            resourceBundle1 = ResourceBundle.getBundle(baseName,operation.getControl(baseName));
            System.out.println("resourceBundle.name : " + resourceBundle1.getString("name"));
        }

    }

    {
        // PropertyResourceBundle -> new PropertyResourceBundle(InputStream);

//        ClassLoader classLoader = ResourceBundleDemo.class.getClassLoader();
//        InputStream inputStream = classLoader.getResourceAsStream("static/default_zh_CN.properties");
//        Reader reader = new InputStreamReader(inputStream,"GBK");
//        ResourceBundle propertyResourceBundle  = new PropertyResourceBundle(reader);
//        System.out.println("propertyResourceBundle.name : " + propertyResourceBundle.getString("name"));
    }


    public static class EncodedControl extends ResourceBundle.Control {

        private final String encoding;

        public EncodedControl(String encoding) {
            this.encoding = encoding;
        }

        public EncodedControl(){
            this("UTF-8");
        }

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format,
                                        ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
            // 资源查找规则
            String bundleName = toBundleName(baseName, locale);
            ResourceBundle bundle = null;
                final String resourceName = toResourceName(bundleName, "properties");
                if (resourceName == null) {
                    return bundle;
                }
                final ClassLoader classLoader = loader;
                final boolean reloadFlag = reload;
                InputStream stream = classLoader.getResourceAsStream(resourceName);
                Reader reader = new InputStreamReader(stream, encoding);
                if (reader != null) {
                    try {
                        bundle = new PropertyResourceBundle(reader);
                    } finally {
                        reader.close();
                        stream.close();
                    }
                }
            return bundle;
        }

    }
}
