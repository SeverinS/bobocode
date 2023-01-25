package com.bobocode.severins.context;

import com.bobocode.severins.context.testbeans.HelloBean;
import com.bobocode.severins.context.testbeans.TestBean;
import com.bobocode.severins.exceptions.NoSuchBeanException;
import com.bobocode.severins.exceptions.NoUniqueBeanException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class ApplicationContextImplTest {
    @Test
    void getBeanByTypeTest(){
        var applicationContext = new ApplicationContextImpl("com.bobocode.severins.context.testbeans");
        var helloBean = applicationContext.getBean(HelloBean.class);
        Assertions.assertEquals("Hello", helloBean.greetings());
    }

    @Test
    void getBeanByNameTest(){
        var applicationContext = new ApplicationContextImpl("com.bobocode.severins.context.testbeans");
        var helloBean = applicationContext.getBean("hello", TestBean.class);
        Assertions.assertEquals("Hello", helloBean.greetings());
        var hiBean = applicationContext.getBean("hiBean", TestBean.class);
        Assertions.assertEquals("Hi", hiBean.greetings());
    }

    @Test
    void getAllBeansTest(){
        var applicationContext = new ApplicationContextImpl("com.bobocode.severins.context.testbeans");
        var beans = applicationContext.getAllBeans(TestBean.class);
        Assertions.assertEquals(2, beans.size());
        Assertions.assertTrue(beans.containsKey("hello"));
        Assertions.assertTrue(beans.containsKey("hiBean"));
    }

    @Test
    void noSuchBeanExceptionTest(){
        var applicationContext = new ApplicationContextImpl("com.bobocode.severins.context.testbeans");
        Assertions.assertThrows(NoSuchBeanException.class, () -> applicationContext.getBean("NoSuchBean", TestBean.class));
    }

    @Test
    void noUniqueBeanExceptionTest(){
        var applicationContext = new ApplicationContextImpl("com.bobocode.severins.context.testbeans");
        Assertions.assertThrows(NoUniqueBeanException.class, () -> applicationContext.getBean(TestBean.class));
    }

}
