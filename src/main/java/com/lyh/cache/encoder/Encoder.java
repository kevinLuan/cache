package com.lyh.cache.encoder;

/**
 * 编码器
 * 
 * @author SHOUSHEN LUAN
 */
public interface Encoder {
  public byte[] encode(Object obj);

  public Object decode(byte[] data);
}
