package com.xxx.rpclite.usage.registry;

import com.xxx.rpclite.server.RpcServiceExporter;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import javax.annotation.Resource;

/**
 * Created by klose on 2017/1/27.
 *
 * 模拟三个server端，端口分别为7777，8888，9999。
 * 现实场景中，三个server应该启动于三个独立的进程中。
 */
@ContextConfiguration("classpath:registry/spring-rpc-server-registry.xml")
public class Server extends AbstractTestNGSpringContextTests {

  @Resource
  private RpcServiceExporter exporter7777;

  @Resource
  private RpcServiceExporter exporter8888;

  @Resource
  private RpcServiceExporter exporter9999;

  @Test
  public void start() throws InterruptedException {
    Thread.sleep(10 * 1000);
    exporter7777.close();

    Thread.sleep(10 * 1000);
    exporter8888.close();

    Thread.sleep(10 * 1000);
    exporter7777.init();

    Thread.sleep(10 * 1000);
    exporter8888.init();

    Thread.sleep(60 * 60 * 1000);
  }
}
