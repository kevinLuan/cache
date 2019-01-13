package com.lyh.cache.encoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class HessianEncode implements Encoder {
	@Override
	public byte[] encode(Object obj) {
		if (obj == null) {
			return null;
		}
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HessianOutput ho = new HessianOutput(os);
		try {
			ho.writeObject(obj);
			return os.toByteArray();
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		} finally {
			try {
				ho.close();
			} catch (IOException e) {
			}
			try {
				os.close();
			} catch (IOException e) {
			}
		}
	}

	@Override
	public Object decode(byte[] data) {
		if (data == null) {
			return null;
		}
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		HessianInput hi = new HessianInput(is);
		try {
			return hi.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			hi.close();
			try {
				is.close();
			} catch (IOException e) {
			}
		}
	}
}
