package com.bobocode.severins.context.testbeans;

import com.bobocode.severins.annotations.Bean;

@Bean("hello")
public class HelloBean implements TestBean{
    @Override
    public String greetings() {
        return "Hello";
    }
}
