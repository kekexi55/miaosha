package com.imooc.miaosha.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {
    @Value(value="com.kexi.name")
    private String name;
    @Value(value = "com.kexi.do")
    private String todo;
    @GetMapping("/test")
    private Object doTest(){
        return name + "," + todo;
    }
}
