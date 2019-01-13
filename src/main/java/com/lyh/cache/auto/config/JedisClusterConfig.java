package com.lyh.cache.auto.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import com.lyh.cache.annotation.Constants;
import com.lyh.cache.utils.GeneratedKey;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class JedisClusterConfig {
  @Autowired
  private Environment environment;

  @Bean
  public JedisPoolConfig createJedisPoolConfig() {
    JedisPoolConfig config = new JedisPoolConfig();
    config.setMaxTotal(getEnvVal(Constants.POOL_MAX_TOTAL, 500));
    config.setMaxIdle(getEnvVal(Constants.POOL_MAX_IDLE, 100));
    config.setMinIdle(getEnvVal(Constants.POOL_MIN_IDLE, 10));
    config.setMaxWaitMillis(getEnvVal(Constants.POOL_MAX_WAITMILLS, 500));
    return config;
  }

  /**
   * 获取环境数据
   * 
   * @param key
   * @param def
   * @return
   */
  private int getEnvVal(String key, int def) {
    return environment.getProperty(key, int.class, def);
  }

  @Bean
  @Autowired
  JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
    JedisConnectionFactory connectionFactory = new JedisConnectionFactory(jedisPoolConfig);
    String hostAndPort = environment.getProperty(Constants.HOST_PORT);
    String pwd = environment.getProperty(Constants.PASSWORD);
    int timeout = getEnvVal(Constants.TIMEOUT, 5000);
    String[] kv = hostAndPort.split(":");
    connectionFactory.setHostName(kv[0]);
    connectionFactory.setPort(Integer.parseInt(kv[1]));
    connectionFactory.setPassword(pwd);
    connectionFactory.setTimeout(timeout);
    return connectionFactory;
  }

  @Bean
  @Autowired
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    return template;
  }

  @Bean
  public GeneratedKey createGeneratedApi() {
    return new GeneratedKey().setPrefix(environment.getProperty(Constants.PREFIX));
  }

//  @Bean
//  public JedisClientApi createJedisClientApi() {
//    return new JedisClientApi();
//  }

}
