package com.xxx.rpclite.service.impl;

import com.xxx.rpclite.entity.POJO;
import com.xxx.rpclite.entity.User;
import com.xxx.rpclite.service.UserService;
import com.xxx.rpclite.service.UserServiceException;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by klose on 2017/1/27.
 */
public class UserServiceImpl implements UserService {
  @Override
  public User getUserById(Long id) {
    return new User("xxx", id.intValue());
  }

  @Override
  public String findUser(String name, Integer age) {
    return name+":"+age;
  }

  @Override
  public String getUserName(long id) throws UserServiceException {
    if (id % 2 == 0){
      UserServiceException ue = new UserServiceException();
      ue.setMsg("expected exception.");
      ue.setErrorCode(404);
      throw ue;
    }else {
      throw new RuntimeException("unexcepted exception.");
    }
  }

  @Override
  public User sendList(List<User> list) {
    for (User user : list){
      System.out.println(user.toString());
    }
    return new User("admin", 777);
  }

  @Override
  public POJO testPojo1(Byte a, Byte[] bs, Date date, Date[] dates) {
    if (a != null){
      System.out.println("a : "+a.toString());
    }
    if (bs != null){
      System.out.println("date : "+date);
    }
    if (dates != null){
      for (Date date1 : dates) {
        System.out.println("dates : "+date1.toString());
      }
    }
    if (bs != null){
      for (Byte b1 : bs){
        System.out.println("bs : "+b1);
      }
    }

    POJO pojo = new POJO();
    pojo.setDate(new Date());
    pojo.setDates(Arrays.asList(new Date(), new Date(new Date().getTime() + 200000)));
//    pojo.setNullValue("nullValue");
//    pojo.setValue("a".getBytes()[0]);
//    pojo.setValues("hello world.".getBytes());

    System.out.println("---------------------------------------------");
    return pojo;
  }
}
