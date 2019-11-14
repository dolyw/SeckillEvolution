package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 首页
 *
 * @author wliduo[i@dolyw.com]
 * @date 2019/1/24 19:27
 */
@RestController
@RequestMapping
public class IndexController {

    /**
     * 首页
     *
     * @param
     * @return java.lang.String
     * @throws
     * @author wliduo[i@dolyw.com]
     * @date 2019/11/14 16:19
     */
    @GetMapping
    public String index() {
        return "Hello World";
    }

}
