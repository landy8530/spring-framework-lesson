package org.landy.reactivewebdemo.util;

public class Utils {

    public static void println(Object value) {
        String threadName = Thread.currentThread().getName();
        System.out.printf("[线程：%s] %s \n",threadName,value);
    }

}
