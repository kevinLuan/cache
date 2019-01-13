package com.lyh.cache.encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 使用JDK序列化方式编码
 */
public class JavaEncode implements Encoder {

	public byte[] encode(Object bean) {
		if (bean == null) {
			return null;
		}
		byte[] result = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(bos);
			oos.writeObject(bean);
			oos.close();
			result = bos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
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
			ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
			obj = ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
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
