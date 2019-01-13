package com.lyh.cache.auto.config;
//package com.hivescm.cache.config;
//
//import java.util.HashSet;
//import java.util.Set;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
//import org.apache.commons.pool2.impl.GenericObjectPool;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//import com.hivescm.cache.utils.CacheLogger;
//import redis.clients.jedis.HostAndPort;
//
//@Component
//@ConfigurationProperties(prefix = "redis.cache")
//public class RedisProperties {
//  /**
//   * 缓存前缀（可以使用项目名称）
//   */
//  private String prefix;
//  /**
//   * 过期时间(秒)
//   */
//  private int expireSeconds;
//  /**
//   * 集群节点
//   */
//  private String clusterNodes;
//  /**
//   * 连接超时时间(单位：毫秒)
//   */
//  private int commandTimeout;
//  /**
//   * 设置Socket阻塞套接字超时时间
//   */
//  private int soTimeout;
//  /**
//   * 最大重试次数
//   */
//  private int maxRedirections;
//  /**
//   * 激活open-cache需要依赖的JedisCluster集群实例
//   */
//  private boolean enableJedisCluster = true;
//  /**
//   * Set the value for the {@code maxTotal} configuration attribute for pools created with this
//   * configuration instance.
//   *
//   * @param maxTotal The new setting of {@code maxTotal} for this configuration instance
//   * @see GenericObjectPool#setMaxTotal(int)
//   */
//  private int maxTotal = 50;
//  /**
//   * Set the value for the {@code maxIdle} configuration attribute for pools created with this
//   * configuration instance.
//   *
//   * @param maxIdle The new setting of {@code maxIdle} for this configuration instance
//   * @see GenericObjectPool#setMaxIdle(int)
//   */
//  private int maxIdle = 10;
//  /**
//   * Set the value for the {@code minIdle} configuration attribute for pools created with this
//   * configuration instance.
//   *
//   * @param minIdle The new setting of {@code minIdle} for this configuration instance
//   * @see GenericObjectPool#setMinIdle(int)
//   */
//  private int minIdle = 1;
//  /**
//   * Set the value for the {@code maxWait} configuration attribute for pools created with this
//   * configuration instance.
//   *
//   * @param maxWaitMillis The new setting of {@code maxWaitMillis} for this configuration instance
//   * @see GenericObjectPool#getMaxWaitMillis()
//   * @see GenericKeyedObjectPool#getMaxWaitMillis()
//   */
//  private int maxWaitMillis = 1000;
//
//  public void check() {
//    if (StringUtils.isBlank(this.clusterNodes)) {
//      throw new IllegalArgumentException("没有找到有效redis配置:`redis.cache.clusterNodes`");
//    }
//    if (StringUtils.isBlank(prefix)) {
//      throw new IllegalArgumentException(
//          "redis配置:`redis.cache.prefix`和项目名`SystemManager.getProjectName()`不能同时为空");
//    }
//    if (commandTimeout < 1) {
//      throw new IllegalArgumentException("无效redis配置:`redis.cache.commandTimeout`");
//    }
//    if (soTimeout < 1) {
//      throw new IllegalArgumentException("无效redis配置:`redis.cache.soTimeout`");
//    }
//    if (expireSeconds < 1) {
//      throw new IllegalArgumentException("无效redis配置:`redis.cache.expireSeconds`");
//    }
//    if (maxRedirections < 1) {
//      throw new IllegalArgumentException("无效redis配置:`redis.cache.maxRedirections`");
//    }
//  }
//
//  public int getSoTimeout() {
//    return soTimeout;
//  }
//
//  public void setSoTimeout(int soTimeout) {
//    this.soTimeout = soTimeout;
//  }
//
//  public int getMaxRedirections() {
//    return maxRedirections;
//  }
//
//  public void setMaxRedirections(int maxRedirections) {
//    this.maxRedirections = maxRedirections;
//  }
//
//  public int getExpireSeconds() {
//    return expireSeconds;
//  }
//
//  public void setExpireSeconds(int expireSeconds) {
//    this.expireSeconds = expireSeconds;
//  }
//
//  public String getClusterNodes() {
//    return clusterNodes;
//  }
//
//  public void setClusterNodes(String clusterNodes) {
//    this.clusterNodes = clusterNodes;
//  }
//
//  public int getCommandTimeout() {
//    return commandTimeout;
//  }
//
//  public void setCommandTimeout(int commandTimeout) {
//    this.commandTimeout = commandTimeout;
//  }
//
//  public String getPrefix() {
//    return prefix;
//  }
//
//  public void setPrefix(String prefix) {
//    this.prefix = prefix;
//  }
//
//  public int getMaxTotal() {
//    return maxTotal;
//  }
//
//  public int getMaxIdle() {
//    return maxIdle;
//  }
//
//  public int getMinIdle() {
//    return minIdle;
//  }
//
//  public int getMaxWaitMillis() {
//    return maxWaitMillis;
//  }
//
//  public void setMaxTotal(int maxTotal) {
//    this.maxTotal = maxTotal;
//  }
//
//  public void setMaxIdle(int maxIdle) {
//    this.maxIdle = maxIdle;
//  }
//
//  public void setMinIdle(int minIdle) {
//    this.minIdle = minIdle;
//  }
//
//  public void setMaxWaitMillis(int maxWaitMillis) {
//    this.maxWaitMillis = maxWaitMillis;
//  }
//
//  /**
//   * 是否激活JedisCluster
//   * 
//   * @return
//   */
//  public boolean isEnableJedisCluster() {
//    if (enableJedisCluster) {
//      if (clusterNodes == null) {
//        CacheLogger.getLogger().warn("[警告]NotFound cache.jar needed config:redis.cache.*");
//        return false;
//      }
//    }
//    return enableJedisCluster;
//  }
//
//  public void setEnableJedisCluster(boolean enableJedisCluster) {
//    this.enableJedisCluster = enableJedisCluster;
//  }
//
//  /**
//   * 获取Redis集群HOST+PORT
//   * 
//   * @return
//   */
//  public Set<HostAndPort> getHostAndPorts() {
//    Set<HostAndPort> nodes = new HashSet<>();
//    if (clusterNodes != null) {
//      String[] serverArray = clusterNodes.split(",");
//      for (String ipPort : serverArray) {
//        String[] ipPortPair = ipPort.split(":");
//        nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
//      }
//    }
//    return nodes;
//  }
//
//}
