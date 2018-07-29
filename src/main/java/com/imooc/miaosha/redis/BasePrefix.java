package com.imooc.miaosha.redis;

public abstract  class BasePrefix implements KeyPrefix {
    private String prefix;
    private int expireSeconds;

    public BasePrefix(String prefix, int expireSeconds) {
        this.prefix = prefix;
        this.expireSeconds = expireSeconds;
    }

    public BasePrefix(String prefix) {
        this.prefix = prefix;
        this.expireSeconds = 0;
    }

    @Override
    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":"+this.prefix;

    }

    @Override
    public int expireSeconds() {//默认0代表永不过期
        return this.expireSeconds;
    }
}
