package com.lyh.cache.utils;

import org.junit.Assert;
import org.junit.Test;
import com.lyh.cache.utils.ParserExpress;
import com.lyh.cache.utils.ParserExpress.Token;

public class ParserExpressTest {
  @Test
  public void test() {
    String expression = "${0.id}1111123@#$%^&*()${}${1.ID,2.name}sdf sdfsdfa${1120.123.nqm}";
    ParserExpress parserExpress = ParserExpress.parse(expression);
    while (parserExpress.hasMoreTokens()) {
      Token token = parserExpress.nextToken();
      System.out.println("token:" + token.getToken());
      parserExpress.updateValue(token, "@️");
    }
    Assert.assertEquals("@️1111123@#$%^&*()@️@️sdf sdfsdfa@️", parserExpress.toString());;

  }
}
