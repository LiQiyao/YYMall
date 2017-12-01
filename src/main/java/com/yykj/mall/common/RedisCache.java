package com.yykj.mall.common;

import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Lee on 2017/8/31.
 */
public class RedisCache implements Cache{

    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private static JedisConnectionFactory jedisConnectionFactory;

    private final String id;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    static {
        ApplicationContext context = new FileSystemXmlApplicationContext("classpath:redis.xml");
        jedisConnectionFactory = (JedisConnectionFactory) context.getBean("jedisConnectionFactory");
        logger.debug("factory:" + jedisConnectionFactory);
    }

    public RedisCache(final String id) {
        if (id == null){
            throw new IllegalArgumentException("Cache instance require an ID");
        }
        logger.debug("RedisCache:ID=" +id);
        this.id = id;

    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public void putObject(Object key, Object value) {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>putObject:"+key+"="+value);
        RedisConnection connection = jedisConnectionFactory.getConnection();
        RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
        connection.set(serializer.serialize(key), serializer.serialize(value));
        connection.close();
    }

    @Override
    public Object getObject(Object key) {
        Object res = null;
        RedisConnection connection = jedisConnectionFactory.getConnection();
        RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
        res = serializer.deserialize(connection.get(serializer.serialize(key)));
        connection.close();
        return res;
    }

    @Override
    public Object removeObject(Object key) {
        RedisConnection connection = jedisConnectionFactory.getConnection();
        RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
        Object result = connection.expire(serializer.serialize(key), 0);
        return result;
    }

    @Override
    public void clear() {
        RedisConnection connection = jedisConnectionFactory.getConnection();
        connection.flushDb();
        connection.flushAll();
        connection.close();
    }

    @Override
    public int getSize() {
        RedisConnection connection = jedisConnectionFactory.getConnection();
        int res = Integer.valueOf(connection.dbSize().toString());
        connection.close();
        return res;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return lock;
    }

}
