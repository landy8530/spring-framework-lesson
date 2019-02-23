package org.landy.reactivewebdemo;

import javax.swing.*;
import java.awt.event.*;

import static org.landy.reactivewebdemo.util.Utils.println;

/**
 * 异步+非阻塞 GUI 实现
 * [线程：main] 启动一个JFrame窗口！
 * [线程：AWT-EventQueue-0] 当前的鼠标位置：x=223,y=218
 * [线程：AWT-EventQueue-0] 当前的鼠标位置：x=223,y=218
 * [线程：AWT-EventQueue-0] 当前的鼠标位置：x=170,y=135
 * [线程：AWT-EventQueue-0] 当前的鼠标位置：x=204,y=139
 * [线程：AWT-EventQueue-0] 销毁当前窗口！
 * [线程：AWT-EventQueue-0] 窗口被关闭，退出程序！
 */
public class AsyncNonBlockingGUIDemo {

    public static void main(String[] args) {
        //Swing Java GUI
        JFrame jFrame = new JFrame();
        //设置标题
        jFrame.setTitle("异步+非阻塞 GUI 实现");
        jFrame.setBounds(300,300,400,400);
        //非阻塞
        //异步：线程被切换 main --> AWT-EventQueue-0
        // 当前程序其实就是Reactive Programming，响应式编程
        //增加一个窗口关闭事件
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                println("销毁当前窗口！");
                jFrame.dispose();
            }

            @Override
            public void windowClosed(WindowEvent e) {
                println("窗口被关闭，退出程序！");
                System.exit(0);//JVM 进程退出
            }
        });
        jFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                println("当前的鼠标位置：x=" + e.getX() + ",y="+e.getY());
            }
        });
        println("启动一个JFrame窗口！");
        //设置可见
        jFrame.setVisible(true);
    }

}
