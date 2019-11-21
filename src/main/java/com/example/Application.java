package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Application
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/7/31 18:00
 */
@SpringBootApplication
@tk.mybatis.spring.annotation.MapperScan("com.example.dao")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
