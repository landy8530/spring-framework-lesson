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
        HttpServletRequest request= mock(HttpServletRequest.class);
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
