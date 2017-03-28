package com.xxx.rpclite.usage.single;

import com.xxx.rpc.service.AccountService;
import com.xxx.rpc.service.UserService;
import com.xxx.rpc.service.entity.Pet;
import com.xxx.rpc.service.entity.User;
import com.xxx.rpc.service.entity.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by klose on 2017/1/27.
 */
@ContextConfiguration("classpath:single/spring-rpc-client-single.xml")
public class Client extends AbstractTestNGSpringContextTests {

  @Autowired
  private UserService userService;

  @Autowired
  private AccountService accountService;

  @Test
  public void test() {
    BigDecimal balance = accountService.getUserBalance(12345678l);
    Assert.assertTrue(balance.longValue() == 12345678l);

    String name = "klsoe";
    String address = "梅龙路龙洲路梅陇七村";
    User user = userService.createUser(name, address);
    Assert.assertNotNull(user);
    Assert.assertNotNull(user.getCreateDate());
    Assert.assertEquals(name, user.getName());
    Assert.assertEquals(address, user.getAddress());

    User user1 = new User(1l, "aaa", "localhost", null);
    User user2 = new User(2l, "bbb", "localhost", null);
    User user3 = new User(3l, "ccc", "localhost", null);
    Integer count = userService.insertUsers(Arrays.asList(user1, user2, user3));
    Assert.assertEquals(count.intValue(), 3);

    List<Pet> pets = userService.getUserPets(1l);
    Assert.assertNotNull(pets);
    Assert.assertEquals(pets.size(), 3);

    pets.forEach(pet -> System.out.println(pet.toString()));

    try {
      userService.getUserById(1l);
    } catch (UserNotFoundException e) {
      String errorMsg = e.getErrorMsg();
      Integer errorCode = e.getErrorCode();
      Assert.assertEquals(errorCode.intValue(), 404);
      Assert.assertEquals(errorMsg, "User Not Found.");
    }

    try {
      userService.getUserById(2l);
    } catch (UserNotFoundException e) {
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("Other UnChecked Exception.");
    }

  }

  @Test
  public void multiThreadTest() {
    int size = 200;
    int loop = 2000;
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
