package org.landy.reactivewebdemo;


import java.util.Observable;

import static org.landy.reactivewebdemo.util.Utils.println;

/**
 * 同步非阻塞，利用观察者模式实现
 * [线程：main] Observable 添加观察者！
 * [线程：main] 通知所有观察者！
 * [线程：main] 3. 收到数据更新：Hello World
 * [线程：main] 2. 收到数据更新：Hello World
 * [线程：main] 1. 收到数据更新：Hello World
 */
public class ObserverPatternDemo {

    public static void main(String[] args) {
        //非阻塞：基本上采用Callback回调的方式
        // 当前实现：同步+非阻塞
        // 同步：线程未切换
        //数据发布
        MyObservable observable = new MyObservable();
        println("Observable 添加观察者！");
        //增加观察者
        //一个Observable对应N个Observer
        observable.addObserver((o,value) -> {
            println("1. 收到数据更新：" + value);
        });
        observable.addObserver((o,value) -> {
            println("2. 收到数据更新：" + value);
        });
        observable.addObserver((o,value) -> {
            println("3. 收到数据更新：" + value);
        });
        //改变
        observable.setChanged();
        //通知观察者
        println("通知所有观察者！");
        observable.notifyObservers("Hello World"); //发送数据 Push Data
    }


    public static class MyObservable extends Observable {
        //需要做字节码提升，protected提升为public
        @Override
        public synchronized void setChanged() {
            super.setChanged();
        }
    }
}
