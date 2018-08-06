package com.imooc.miaosha.redis;

public class MiaoshaUserKey extends BasePrefix {
    private static final int expireSeconds = 3600*24;

    public MiaoshaUserKey(String prefix, int expireSeconds) {
        super(prefix, expireSeconds);
    }

    public static MiaoshaUserKey token = new MiaoshaUserKey("tk",expireSeconds);
    public static MiaoshaUserKey getUserById = new MiaoshaUserKey("id",0);
}
