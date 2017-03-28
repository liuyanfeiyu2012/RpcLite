package com.xxx.rpclite.service;

import com.xxx.rpclite.annotation.RpcLite;
import com.xxx.rpclite.entity.POJO;
import com.xxx.rpclite.entity.User;

import java.util.Date;
import java.util.List;

/**
 * Created by klose on 2017/1/27.
 */
@RpcLite(serviceName = "userServiceImpl", timeout = 5)
public interface UserService {

  @RpcLite(timeout = 10)
  User getUserById(Long id);

  @RpcLite(methodName = "findUser")
  String findUser(String name, Integer age);

  @RpcLite(retry = 0)
  String getUserName(long id) throws UserServiceException;

  User sendList(List<User> list);


  POJO testPojo1(Byte a, Byte[] b, Date date, Date[] dates);

}
