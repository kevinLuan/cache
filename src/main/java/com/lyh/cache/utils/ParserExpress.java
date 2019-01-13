package com.lyh.cache.utils;

import java.util.ArrayList;
import java.util.List;

public class ParserExpress {
  private StringBuilder builder;
  private List<Token> tokens = new ArrayList<Token>();

  public static ParserExpress parse(String expression) {
    return new ParserExpress(expression);
  }

  public ParserExpress(String expression) {
    this.builder = new StringBuilder(expression);
    parserExpress();
  }

  private void parserExpress() {
    int startIndex = builder.indexOf("${");
    while (startIndex >= 0) {
      int endIndex = builder.indexOf("}", startIndex);
      if (endIndex == -1) {
        System.err.print("表达式没有闭合--startIndex:" + startIndex);
        break;
      }
      String name = builder.substring(startIndex + 2, endIndex);
      builder.delete(startIndex, endIndex + 1);
      tokens.add(new Token(startIndex, name));
      startIndex = builder.indexOf("${");
    }
  }

  private int index = -1;

  public boolean hasMoreTokens() {
    if (index == -1) {
      index = tokens.size();
    }
    if (!tokens.isEmpty()) {
      return index > 0;
    }
    return false;
  }

  public Token nextToken() {
    if (hasMoreTokens()) {
      index--;
      return tokens.get(index);
    }
    return null;
  }

  public ParserExpress updateValue(Token token, String value) {
    builder.insert(token.getPos(), value);
    return this;
  }

  public String toString() {
    return builder.toString();
  }

  public class Token {
    private int pos = -1;
    private String token;

    public Token(int pos, String token) {
      this.pos = pos;
      this.token = token;
    }

    public int getPos() {
      return this.pos;
    }

    public String getToken() {
      return token;
    }
  }

}
