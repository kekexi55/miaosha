package com.imooc.miaosha.controller;

import com.imooc.miaosha.domain.User;
import com.imooc.miaosha.rabbitmq.MQSender;
import com.imooc.miaosha.redis.RedisService;
import com.imooc.miaosha.redis.UserKey;
import com.imooc.miaosha.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/demo")
public class SampleController {
    @Autowired
    UserService userService;
    @Autowired
    RedisService redisService;
    @Autowired
    private MQSender mqSender;
    @RequestMapping("/themeleaf")
    public String themeleaf(Model model){
        model.addAttribute("name","chunguang");
        return "hello";
    }

    @RequestMapping("/db/get/")
    @ResponseBody
    public void dbGet(){
        User user = userService.getById(1);
        System.out.println(((User) user).getName());
    }

    @RequestMapping("/db/dbtx/")
    @ResponseBody
    public void dbTx(){
        userService.tx();
    }

    @RequestMapping("/redis/get/")
    @ResponseBody
    public void redisGet(){
        String v1= redisService.get(UserKey.getById,"111",String.class);
        System.out.println(v1);
    }

    @RequestMapping("/redis/set/")
    @ResponseBody
    public void redisSet(){
        String v2= redisService.get(UserKey.getById,"222",String.class);
        System.out.println(v2);
    }


}
