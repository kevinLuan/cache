package com.lyh.cache.service.test;

import java.io.InputStream;
import java.util.Properties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class ConfigurationPropertiesPostProcessor implements BeanPostProcessor, InitializingBean {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    return bean;
  }


  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }


  @Override
  public void afterPropertiesSet() throws Exception {
    PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    InputStream in = resolver.getResource("classpath:application.properties").getInputStream();
    if (in != null) {
      Properties properties = new Properties();
      properties.load(in);
      properties.keySet().forEach((key) -> {
        String value = properties.getProperty((String) key);
        System.setProperty((String) key, value);
      });
    }
  }
}
