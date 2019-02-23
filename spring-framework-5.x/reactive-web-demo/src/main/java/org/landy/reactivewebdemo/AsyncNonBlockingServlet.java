package org.landy.reactivewebdemo;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.landy.reactivewebdemo.util.Utils.println;

/**
 * 异步+非阻塞Servlet
 */
@WebServlet(name = "async-non-blocking",
        urlPatterns = "/async-non-blocking",
        asyncSupported = true
)
public class AsyncNonBlockingServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //开启异步上下文
        AsyncContext asyncContext = req.startAsync();
        println("异步上下文执行开始...");
        //非阻塞回调实现
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                ServletResponse response = event.getSuppliedResponse();
                ServletOutputStream outputStream = response.getOutputStream();
                //以下代码如果加上下面执行的非阻塞I/O则会抛出异常：
                // java.lang.IllegalStateException: getOutputStream() has already been called for this response
                //response.getWriter().println("Hello,World 1!");
                //真正的非阻塞I/O
                //非阻塞I/O不能放在非阻塞回调中执行
//                outputStream.setWriteListener(new WriteListener() {
//                    @Override
//                    public void onWritePossible() throws IOException {
//                        outputStream.println("Hello,World 2!");
//                        println("异步 + 非阻塞执行完毕...");
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//
//                    }
//                });

                outputStream.println("Hello,World 1!");
                println("异步上下文执行完毕...");
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {

            }

            @Override
            public void onError(AsyncEvent event) throws IOException {

            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {

            }
        });
        /**
         * [线程：http-nio-8080-exec-3] 异步上下文执行开始...
         * [线程：http-nio-8080-exec-4] 异步上下文触发结束...
         * [线程：http-nio-8080-exec-3] 异步 + 非阻塞执行完毕...
         * [线程：http-nio-8080-exec-3] 异步上下文执行完毕...
         */
        ServletOutputStream outputStream = resp.getOutputStream();
        //真正的非阻塞I/O
        outputStream.setWriteListener(new WriteListener() {
            @Override
            public void onWritePossible() throws IOException {
                outputStream.println("Hello,World 2!");
                println("异步 + 非阻塞执行完毕...");
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        //完成操作
        //实现了异步+非阻塞
        /**
         * 线程进行了上下文切换
         * [线程：http-nio-8080-exec-2] 异步上下文执行开始...
         * [线程：http-nio-8080-exec-1] 异步上下文执行结束...
         * [线程：http-nio-8080-exec-4] 异步上下文执行完毕...
         */
        asyncContext.start(() ->{
            println("异步上下文触发结束...");
            asyncContext.complete();
        });

        //如果只是这样执行，则只是非阻塞实现
        /**
         * 线程未切换
         * [线程：http-nio-8080-exec-1] 异步上下文执行开始...
         * [线程：http-nio-8080-exec-1] 异步上下文执行结束...
         * [线程：http-nio-8080-exec-1] 异步上下文执行完毕...
         */
        //println("异步上下文触发结束...");
        //asyncContext.complete();
    }
}
