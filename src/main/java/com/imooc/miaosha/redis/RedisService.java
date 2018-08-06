package com.imooc.miaosha.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class RedisService {
    @Autowired
    JedisPool jedisPool;

    public <T>T get(KeyPrefix prefix,String key,Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = jedis.get(prefix.getPrefix()+key);
            T t = strToBean(str,clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
        }

    private void returnToPool(Jedis jedis){
        if(jedis!=null){
            jedis.close();
        }
    }

    public <T>boolean set(KeyPrefix prefix,String key,T value){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            String str = beanToString(value);
            if(str == null || str.length() ==0){
                return false;
            }
            int exprieSeconds = prefix.expireSeconds();
            if(exprieSeconds <= 0){
                jedis.set(prefix.getPrefix()+key,str);
            }else{
                jedis.setex(prefix.getPrefix()+key,exprieSeconds,str);
            }
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            long ret = jedis.del(prefix.getPrefix()+key);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }

    private <T>T strToBean(String str,Class<T> clazz){
        if(str ==null || str.length()<=0 ||clazz ==null){
            return null;
        }
        if(clazz == int.class || clazz == Integer.class){
            return (T)Integer.valueOf(str);
        }else if(clazz == long.class || clazz == Long.class){
            return (T)Long.valueOf(str);
        }else if(clazz == String.class){
            return (T)str;
        }else{
            return JSON.toJavaObject(JSON.parseObject(str),clazz);
        }
    }

    private  <T>String beanToString(T value){
        if(value ==null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if(clazz == int.class || clazz == Integer.class){
            return value+"";
        }else if(clazz == long.class || clazz == Long.class){
            return value+"";
        }else if(clazz == String.class){
            return (String)value;
        }else{
            return JSON.toJSONString(value);
        }
    }


    public <T>boolean exists(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.exists(prefix.getPrefix()+key);
        }finally {
            returnToPool(jedis);
        }
    }

    public <T>Long incr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.incr(prefix.getPrefix()+key);
        }finally {
            returnToPool(jedis);
        }
    }

    public <T>Long decr(KeyPrefix prefix,String key){
        Jedis jedis = null;
        try{
            jedis = jedisPool.getResource();
            return jedis.decr(prefix.getPrefix()+key);
        }finally {
            returnToPool(jedis);
        }
    }


}
