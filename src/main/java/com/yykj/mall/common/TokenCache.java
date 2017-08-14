package com.yykj.mall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Lee on 2017/8/14.
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    public static final String TOKEN_PREFIX = "token_";

    private static LoadingCache<String, String> loadingCache = CacheBuilder.newBuilder()
            .initialCapacity(1024).maximumSize(10000).expireAfterAccess(2, TimeUnit.HOURS).build(
                    new CacheLoader<String, String>() {
                        //重写如果没有key对应的值得时候，让其返回字符串的"null"，防止抛出异常
                        @Override
                        public String load(String s) throws Exception {
                            return "null";
                        }
                    }
            );
    public static void addKeyAndValue(String key, String value){
        loadingCache.put(key, value);
    }

    public static String getValue(String key){
        String value = null;
        try {
            value = loadingCache.get(key);
            if ("null".equals(value)){
                return null;
            }
            return value;
        } catch (ExecutionException e) {
            logger.error("从缓存中获取token失败！", e);
        }
        return null;
    }
}
