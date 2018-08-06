package com.imooc.miaosha.redis;

public class OrderKey extends BasePrefix {
    public OrderKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }
    public static OrderKey order = new OrderKey("order",0);
}
