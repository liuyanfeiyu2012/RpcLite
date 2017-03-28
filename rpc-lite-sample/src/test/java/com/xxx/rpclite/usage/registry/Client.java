package com.xxx.rpclite.usage.registry;

import com.xxx.rpc.service.AccountService;
import com.xxx.rpc.service.UserService;
import com.xxx.rpc.service.entity.Pet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by klose on 2017/1/27.
 */
@ContextConfiguration("classpath:registry/spring-rpc-client-registry.xml")
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

}
