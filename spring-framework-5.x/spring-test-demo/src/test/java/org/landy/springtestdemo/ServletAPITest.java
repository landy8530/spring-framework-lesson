package org.landy.springtestdemo;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Servlet API 测试
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/18
 */
public class ServletAPITest {

    @Test
    public void testHttpServletRequestInDynamicMock() {
        // 动态代理 HttpServletRequest
        // JDK版本需要1.8.0_202以上
        HttpServletRequest request= mock(HttpServletRequest.class);
        System.out.println(request); //Mock for HttpServletRequest, hashCode: 1259174396 可以知道是代理对象
        // 当需要调用 HttpServletRequest#getParameter 时，并且参数名称为"name"
        when(request.getParameter("name")).thenReturn("Landy");

        String value = request.getParameter("name");

        assertEquals("Landy",value);
    }

    @Test
    public void testHttpServletRequestInStaticMock() {

        MockHttpServletRequest request = new MockHttpServletRequest();

        request.setParameter("name","Landy");
        // 获取 请求参数
        // 没有 Web 服务，也没有 Tomcat，也没有 Spring Boot
        String value = request.getParameter("name");

        assertEquals("Landy",value);
    }

}
