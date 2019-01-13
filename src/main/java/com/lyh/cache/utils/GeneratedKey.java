package com.lyh.cache.utils;

import java.lang.reflect.Field;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import com.lyh.cache.annotation.Cached;

public class GeneratedKey {
  /**
   * 追加数据库Key前缀
   */
  private String prefix;

  private String makeDynamicKey(String params, Object[] args) throws Throwable {
    StringBuilder builder = new StringBuilder(20);
    String[] strs = params.replaceAll("\\s*", "").split(",");
    for (int i = 0; i < strs.length; i++) {
      if (StringUtils.isNotEmpty(strs[i])) {
        if (strs[i].indexOf(".") == -1) {
          Object val = args[Integer.parseInt(strs[i])];
          builder.append(asString(val));
        } else {
          String[] el = strs[i].split("\\.");
          if (el.length == 2) {
            int index = Integer.parseInt(el[0]);
            Object obj = args[index];
            Object val = getFieldValue(obj, el[1]);
            builder.append(asString(val));
          } else {
            throw new IllegalArgumentException("parser “" + strs[i] + "” error...");
          }
        }
      }
    }
    return builder.toString();
  }

  private String asString(Object value) {
    if (value == null) {
      return "_NULL";
    }
    return "_" + value.toString();
  }

  private Object getFieldValue(Object obj, String fieldName) throws Throwable {
    if (StringUtils.isNotEmpty(fieldName)) {
      if (Map.class.isAssignableFrom(obj.getClass())) {
        Map<?, ?> map = (Map<?, ?>) obj;
        if (map.containsKey(fieldName)) {
          return map.get(fieldName);
        } else {
          for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (String.valueOf(entry.getKey()).equals(fieldName)) {
              return entry.getValue();
            }
          }
        }
      } else {
        Field field = FieldUtils.getField(obj.getClass(), fieldName, true);
        if (field == null) {
          String protoFieldName = fieldName + "_";
          field = FieldUtils.getField(obj.getClass(), protoFieldName, true);
        }
        if (field != null) {
          return field.get(obj);
        }
      }
    }
    throw new IllegalArgumentException("fieldName is empty or not found:`" + fieldName + "`");
  }

  public String buildKey(ProceedingJoinPoint pjp, Cached cache) throws Throwable {
    return this.buildKey(pjp, cache.key(), cache.params());
  }

  public String getPrefix() {
    return prefix;
  }

  public GeneratedKey setPrefix(String prefix) {
    this.prefix = prefix;
    return this;
  }

  public String buildKey(ProceedingJoinPoint pjp, String key, String param) throws Throwable {
    if (key == null || key.length() == 0) {
      key = pjp.getTarget().getClass().getSimpleName();
    }
    String paramKey = null;
    if (param == null || param.length() > 0) {
      paramKey = makeDynamicKey(param, pjp.getArgs());
    } else {
      MethodSignature method = (MethodSignature) pjp.getSignature();
      paramKey = method.getName();
    }
    return formatKey(key, paramKey);
  }

  private String formatKey(String key, String paramKey) {
    if (StringUtils.isBlank(prefix)) {
      return key + ":" + paramKey;
    } else {
      return prefix + ":" + key + paramKey;
    }
  }

  /**
   * 生成缓存key
   * 
   * @param key
   * @param params
   * @return
   */
  public String buildKey(String key, Object... params) {
    StringBuilder builder = new StringBuilder(200);
    for (Object arg : params) {
      builder.append(asString(arg));
    }
    return formatKey(key, builder.toString());
  }

}
