package com.xxx.rpclite.usage.fix;

import com.xxx.rpc.service.AccountService;
import com.xxx.rpc.service.UserService;
import com.xxx.rpc.service.entity.Pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by klose on 2017/1/27.
 */
@ContextConfiguration("classpath:fix/spring-rpc-client-fix.xml")
public class Client extends AbstractTestNGSpringContextTests {

  @Autowired
  private UserService userService;

  @Autowired
  private AccountService accountService;

  @Test
  public void test() throws InterruptedException {

    while (true) {
      BigDecimal balance = accountService.getUserBalance(12345678l);
      Assert.assertTrue(balance.longValue() == 12345678l);
      Thread.sleep(500);
      List<Pet> pets = userService.getUserPets(1l);
      Assert.assertNotNull(pets);
      Thread.sleep(500);
    }

  }

  @Test
  public void multiThreadTest() {
    int size = 200;
    int loop = 5000;
    List<Worker> workers = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      Worker worker = new Worker(i, loop);
      workers.add(worker);
    }
    long start = System.currentTimeMillis();

    for (Worker worker : workers) {
      worker.start();
    }

    for (Worker worker : workers) {
      try {
        worker.join();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    long end = System.currentTimeMillis();
    int total = size * loop;
    long last = end - start;
    System.out.println("共调用方法 "+total+" 次，耗时 "+last+" 毫秒，TPS："+(total * 1000)/last+"。");
  }

  class Worker extends Thread {

    private Integer Id;

    private Integer loop;

    public Worker(Integer id, Integer loop) {
      this.Id = id;
      this.loop = loop;
    }

    @Override
    public void run() {
      for (int i = 0; i < loop; i++) {
        List<Pet> pets = userService.getUserPets(1l);
        Assert.assertNotNull(pets);
        Assert.assertEquals(pets.size(), 3);
      }
      System.out.println("Worker "+ Id+" Complete Work.");
    }
  }

}
