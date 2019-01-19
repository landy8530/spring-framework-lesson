package org.landy.springtestdemo;

import org.junit.Test;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.mock.env.MockEnvironment;

/**
 * {@link Environment} Test
 *
 * @author Landy
 * @copyright Landy
 * @since 2018/1/18
 */
public class EnvironmentTest {

    @Test
    public void testGetProperty(){
        MockEnvironment environment = new MockEnvironment();
        environment.setProperty("os.name","Windows 7");
        System.out.println(environment.getProperty("os.name"));
        System.out.println(System.getProperty("os.name"));
        System.out.println(System.getProperty("TMP"));
    }

    @Test
    public void testStandardEnvironment(){
        StandardEnvironment environment = new StandardEnvironment();
        System.out.println(environment.getProperty("TMP"));
        System.out.println(environment.getProperty("path"));
    }

}
