package com.lyh.cache.encoder;

import java.io.Serializable;
import java.math.BigDecimal;

import com.google.gson.Gson;

class User implements Serializable {
	int num = 1;
	BigDecimal bigDecimal = new BigDecimal("12.1221");
	boolean bool = true;
	float f = 1.234f;
	double d = 1.123d;

	@Override
	public String toString() {
		return new Gson().toJson(this);
	}
}
