package com.bobocode.severins.context.testbeans;

import com.bobocode.severins.annotations.Bean;

@Bean
public class HiBean implements TestBean{
    @Override
    public String greetings() {
        return "Hi";
    }
}
