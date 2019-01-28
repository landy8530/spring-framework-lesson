package org.landy.javai18ndemo.javase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * {@link DateFormat} 示例
 * @author Landy
 * @copyright Landy
 * @since 2018/1/28
 */
public class DateFormatDemo implements Runnable {

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public static void main(String[] args) {
        new Thread(new DateFormatDemo()).start();
        new Thread(new DateFormatDemo()).start();
        new Thread(new DateFormatDemo()).start();
        new Thread(new DateFormatDemo()).start();
        new Thread(new DateFormatDemo()).start();
        new Thread(new DateFormatDemo()).start();
    }


    @Override
    public void run() { // 重进入 ReentrantLock
        //DateFormat是线程安全的
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        System.out.println(dateFormat.format(new Date()));
    }
}
