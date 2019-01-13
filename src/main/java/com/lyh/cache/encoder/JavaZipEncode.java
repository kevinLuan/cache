package com.lyh.cache.encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.lyh.cache.utils.CacheLogger;

/**
 * 使用JDK序列化方式编码，且支持解压缩处理
 */
public class JavaZipEncode implements Encoder {

	public byte[] encode(Object bean) {
		if (bean == null) {
			return null;
		}
		byte[] result = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new GZIPOutputStream(bos));
			oos.writeObject(bean);
			oos.close();
			result = bos.toByteArray();
		} catch (IOException e) {
			CacheLogger.getLogger().error("encode(" + bean + ")ERROR", e);
		} finally {
			try {
				if (null != oos) {
					oos.close();
				}
				bos.close();
			} catch (IOException e) {
			}
		}
		return result;
	}

	public Object decode(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		Object obj = null;
		ObjectInputStream ois = null;
		try {
			ois = new ObjectInputStream(new GZIPInputStream(new ByteArrayInputStream(bytes)));
			obj = ois.readObject();
		} catch (Exception e) {
			CacheLogger.getLogger().error("encode()ERROR", e);
		} finally {
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (IOException e) {
			}
		}
		return obj;
	}

}
