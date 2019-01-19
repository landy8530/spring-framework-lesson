package org.landy.springtestdemo;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * JUnit 5
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/18
 */
public class JUnit5Test {

    /**
     * @BeforeEach：等同于 JUnit 4 @Before
     * @AfterEach：等同于 JUnit 4 @After
     * @BeforeAll：等同于 JUnit 4 @BeforeClass
     * @AfterAll：等同于 JUnit 4 @AfterClass
     */
    @BeforeEach
    public void prepare() {
        System.out.println("JUnit 5 方式：准备数据源");
    }

    /**
     * JUnit 5 的测试方式
     */
    @Test
    public void testHelloWorld() {
        System.out.println("HelloWorld");
    }

    /**
     * JUnit 5 的测试方式
     */
    @Test
    public void testHelloWorld2() {
        System.out.println("HelloWorld2");
    }

    @RepeatedTest(value = 5)
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5})
    public void test100Times(int id) {
        Assert.assertTrue(id > -1);
    }
}
