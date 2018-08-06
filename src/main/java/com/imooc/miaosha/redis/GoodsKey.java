package com.imooc.miaosha.redis;

public class GoodsKey extends BasePrefix {

    private GoodsKey(String prefix) {
        super(prefix);
    }
    private GoodsKey(String prefix, int expireSeconds) {
        super(prefix,expireSeconds);
    }
    public static GoodsKey getGoodsList = new GoodsKey("gl",60);
    public static GoodsKey getGoodsDetail = new GoodsKey("gd",60);
    public static GoodsKey getGoodsStock = new GoodsKey("gs",0);
}
