package com.blackflag.hongrybot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class HongrybotApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(HongrybotApplication.class, args);
    }

}
