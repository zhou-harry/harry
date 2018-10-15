package com.harry.fssc.config;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheManager.RedisCacheManagerBuilder;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisNode.NodeType;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.harry.fssc.redis.RedisObjectSerializer;
import com.harry.fssc.util.Const;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)//Session过期时间，单位是秒
public class RedisConfig extends CachingConfigurerSupport {
	private static Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		RedisClusterConfiguration clusterConfiguration = new RedisClusterConfiguration();
		
		clusterConfiguration.addClusterNode(new RedisNode("192.168.234.128", 7001));
		clusterConfiguration.addClusterNode(new RedisNode("192.168.234.128", 7002));
		clusterConfiguration.addClusterNode(new RedisNode("192.168.234.128", 7003));
		clusterConfiguration.addClusterNode(new RedisNode("192.168.234.128", 7004));
		clusterConfiguration.addClusterNode(new RedisNode("192.168.234.128", 7005));
		clusterConfiguration.addClusterNode(new RedisNode("192.168.234.128", 7006));
		
//		clusterConfiguration.setPassword(RedisPassword.of("123456"));		

		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(10);
		poolConfig.setMaxIdle(8);

		JedisConnectionFactory factory = new JedisConnectionFactory(clusterConfiguration);
		return factory;
	}
	
	@Bean
	public CacheManager cacheManager(RedisConnectionFactory factory) {

		RedisCacheManagerBuilder builder = RedisCacheManager.builder(factory);

		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMillis(Const.GLOBAL_SESSION_TIMEOUT))
				.disableCachingNullValues();

		builder.cacheDefaults(config);

		builder.transactionAware();

		return builder.build();
	}

	@Bean
	public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate template = new StringRedisTemplate(factory);
        template.setValueSerializer(new RedisObjectSerializer());
		return template;
	}

	@Bean
	public KeyGenerator harryKeyGenerator() {
		/**
		 * 定义缓存数据 key 生成策略的bean 包名+类名+方法名+所有参数
		 */
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {

				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};

	}

}
