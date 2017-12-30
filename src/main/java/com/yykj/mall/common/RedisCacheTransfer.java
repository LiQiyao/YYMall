package com.yykj.mall.common;

import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * @author Lee
 * @date 2017/12/30
 */
public class RedisCacheTransfer {

    public RedisCacheTransfer(JedisConnectionFactory jedisConnectionFactory) {
        System.out.println("hhh");
        RedisCache.setJedisConnectionFactory(jedisConnectionFactory);
    }

    public void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        System.out.println("hhh");
        RedisCache.setJedisConnectionFactory(jedisConnectionFactory);
    }
}
