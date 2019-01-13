package com.lyh.cache.error;

public class NotSupportException extends RuntimeException {
  private static final long serialVersionUID = 6540946657534537041L;

  public NotSupportException(String msg) {
    super(msg);
  }
}
