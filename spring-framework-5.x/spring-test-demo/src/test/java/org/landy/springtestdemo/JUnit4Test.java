package org.landy.springtestdemo;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 测试
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/18
 */
public class JUnit4Test
//        extends TestCase
{

    protected void setUp() throws Exception {
        System.out.println("JUnit 3 方式：准备数据源");
    }

    protected void tearDown() throws Exception {
        System.out.println("JUnit 3 方式：准备数据源");
    }

    @Before
    public void prepare() {
        System.out.println("JUnit 4 方式：准备数据源");
    }

    @After
    public void destroy() {
        System.out.println("JUnit 4 方式：关闭数据源");
    }

    /**
     * JUnit 3 的测试方式
     */
    public void testHelloWorld2() {
        System.out.println("HelloWorld2");
    }

    /**
     * JUnit 4 的测试方式
     */
    @Test
    public void testHelloWorld() {
        System.out.println("HelloWorld");
    }

    /**
     * JUnit 4 的测试方式
     */
    @Test
    public void testValue() {
        System.out.println("Value");
    }

    @Test
    public void test100Times() {
        for (int i = 0; i < 100; i++) {
            Assert.assertTrue(i > -1);
        }
    }

}
