# Spring Framework

## 多JDK版本说明

以前安装JDK，需要手动配置环境变量。JDK8以后多了自动配置环境变量，所以可以不用手动配置。如果我已经装了JDK8，还想再装一个JDK9，安装完，自动配置的环境变量会指向JDK9版本。

### 解决方法

#### 1. 删除自动配置的环境变量 

自动配置的环境变量是一个隐藏目录：`C:\ProgramData\Oracle\Java\javapath`，删掉这个目录下的3个exe文件，系统就无法匹配到了。 

#### 2. 手动配置环境变量 

自动匹配是匹配不到了，所以我们用老办法，手动配置环境变量即可。这样，我们可以根据环境变量里配置的JDK版本去实现版本切换了。

## Spring-framework-4.x

[Spring-framework-4.x](spring-framework-4.x/README.md) 

## Spring-framework-5.x

[Spring-framework-5.x](spring-framework-5.x/README.md) 

[Spring Aware接口应用](spring-aware.md) 

