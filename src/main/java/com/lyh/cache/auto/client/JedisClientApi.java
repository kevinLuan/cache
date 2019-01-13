package com.lyh.cache.auto.client;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands.SetOption;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;
import com.lyh.cache.encoder.Encoder;
import com.lyh.cache.encoder.HessianEncode;

@Component
public class JedisClientApi {
  @Autowired
  private JedisConnectionFactory factory;
  private Encoder encoder = new HessianEncode();

  public byte[] get(String key) {
    return CallCommand.call(new Exec<byte[]>(factory) {
      @Override
      public byte[] execute(RedisConnection connection) {
        byte[] value = connection.get(key.getBytes());
        return value;
      }
    });
  }

  public String getAsString(String key) {
    byte[] data = this.get(key);
    if (data != null && data.length > 0) {
      return new String(data);
    }
    return null;
  }

  public Integer getAsInt(String key, Integer def) {
    try {
      String val = this.getAsString(key);
      if (val != null) {
        return Integer.parseInt(val);
      }
    } catch (Exception e) {
    }
    return def;
  }
  
  public Long getAsLong(String key, Long def) {
    try {
      String val = this.getAsString(key);
      if (val != null) {
        return Long.parseLong(val);
      }
    } catch (Exception e) {
    }
    return def;
  }

  public Double getAsDouble(String key, Double def) {
    try {
      String val = this.getAsString(key);
      if (val != null) {
        return Double.parseDouble(val);
      }
    } catch (Exception e) {
    }
    return def;
  }

  @SuppressWarnings("unchecked")
  public <T> T getObject(String key) {
    return CallCommand.call(new Exec<T>(factory) {
      @Override
      public T execute(RedisConnection connection) {
        byte[] value = connection.get(key.getBytes());
        if (value != null) {
          return (T) encoder.decode(value);
        }
        return null;
      }
    });
  }

  public Boolean setIfAbsent(String key, Object value) {
    return CallCommand.call(new Exec<Boolean>(factory) {
      @Override
      public Boolean execute(RedisConnection connection) {
        return connection.setNX(key.getBytes(), encoder.encode(value));
      }
    });
  }

  public void set(String key, Object value) {
    CallCommand.call(new Exec<Void>(factory) {
      @Override
      public Void execute(RedisConnection connection) {
        connection.set(key.getBytes(), encoder.encode(value));
        return null;
      }
    });
  }

  public Boolean hSet(String key, String field, Object value) {
    return CallCommand.call(new Exec<Boolean>(factory) {
      @Override
      public Boolean execute(RedisConnection connection) {
        return connection.hSet(key.getBytes(), field.getBytes(), encoder.encode(value));
      }
    });
  }

  public Boolean hSetNX(String key, String field, Object value) {
    return CallCommand.call(new Exec<Boolean>(factory) {
      @Override
      public Boolean execute(RedisConnection connection) {
        return connection.hSetNX(key.getBytes(), field.getBytes(), encoder.encode(value));
      }
    });
  }

  @SuppressWarnings("unchecked")
  public <T> T hGet(String key, String field) {
    return CallCommand.call(new Exec<T>(factory) {
      @Override
      public T execute(RedisConnection connection) {
        byte[] data = connection.hGet(key.getBytes(), field.getBytes());
        if (data != null) {
          return (T) encoder.decode(data);
        }
        return null;
      }
    });
  }

  public Boolean hExists(String key, String field) {
    return CallCommand.call(new Exec<Boolean>(factory) {
      @Override
      public Boolean execute(RedisConnection connection) {
        return connection.hExists(key.getBytes(), field.getBytes());
      }
    });
  }

  public Boolean exists(String key) {
    return CallCommand.call(new Exec<Boolean>(factory) {
      @Override
      public Boolean execute(RedisConnection connection) {
        return connection.exists(key.getBytes());
      }
    });
  }

  public Long hLen(String key) {
    return CallCommand.call(new Exec<Long>(factory) {
      @Override
      public Long execute(RedisConnection connection) {
        return connection.hLen(key.getBytes());
      }
    });
  }

  public Long hDel(String key, final String... fileds) {
    return CallCommand.call(new Exec<Long>(factory) {
      @Override
      public Long execute(RedisConnection connection) {
        byte[][] byteFields = new byte[fileds.length][];
        for (int i = 0; i < fileds.length; i++) {
          byteFields[i] = fileds[i].getBytes();
        }
        return connection.hDel(key.getBytes(), byteFields);
      }
    });
  }

  public void set(String key, Object value, int expire) {
    CallCommand.call(new Exec<Void>(factory) {
      @Override
      public Void execute(RedisConnection connection) {
        connection.set(key.getBytes(), encoder.encode(value), Expiration.seconds(expire),
            SetOption.upsert());
        return null;
      }
    });
  }

  public long delete(String... keys) {
    return CallCommand.call(new Exec<Long>(factory) {
      @Override
      public Long execute(RedisConnection connection) {
        byte[][] keys_byte = new byte[keys.length][];
        for (int i = 0; i < keys.length; i++) {
          String k = keys[i];
          keys_byte[i] = k.getBytes();
        }
        return connection.del(keys_byte);
      }
    });
  }

  public String[] hKeys(String key) {
    return CallCommand.call(new Exec<String[]>(factory) {
      @Override
      public String[] execute(RedisConnection connection) {
        Set<byte[]> set = connection.hKeys(key.getBytes());
        String[] fields = new String[set.size()];
        int index = 0;
        for (byte[] data : set) {
          fields[index] = new String(data);
          index++;
        }
        return fields;
      }
    });
  }

  /**
   * 设置过期时间
   * 
   * @param key
   * @param seconds
   */
  public void setExpire(String key, long seconds) {
    CallCommand.call(new Exec<Void>(factory) {
      @Override
      public Void execute(RedisConnection connection) {
        connection.expire(key.getBytes(), seconds);
        return null;
      }
    });
  }

  public long incr(String key) {
    return this.incr(key, 1);
  }

  public long incr(String key, long value) {
    return CallCommand.call(new Exec<Long>(factory) {
      @Override
      public Long execute(RedisConnection connection) {
        return connection.incrBy(key.getBytes(), value);
      }
    });
  }

  public long decr(String key) {
    return this.decr(key, 1);
  }

  public long decr(String key, long value) {
    return CallCommand.call(new Exec<Long>(factory) {
      @Override
      public Long execute(RedisConnection connection) {
        return connection.decrBy(key.getBytes(), value);
      }
    });
  }

  static class CallCommand<T> {
    public static <T> T call(Exec<T> exec) {
      return exec.doExecute();
    }
  }
  abstract class Exec<T> {
    JedisConnectionFactory factory;

    public Exec(JedisConnectionFactory factory) {
      this.factory = factory;
    }

    public T doExecute() {
      RedisConnection connection = factory.getConnection();
      try {
        return execute(connection);
      } finally {
        connection.close();
      }
    }

    public abstract T execute(RedisConnection connection);
  }
}
