package com.xxx.rpclite.usage.single;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

/**
 * Created by klose on 2017/1/27.
 */
@ContextConfiguration("classpath:single/spring-rpc-server-single.xml")
public class Server extends AbstractTestNGSpringContextTests {

  @Test
  public void start() throws InterruptedException {
    Thread.sleep(60 * 60 * 1000);
  }
}
