package com.xxx.rpc.service;

import com.xxx.rpc.service.entity.User;
import com.xxx.rpc.service.entity.UserNotFoundException;
import com.xxx.rpclite.annotation.RpcLite;

import java.util.List;

/**
 * Created by klose on 2017/1/27.
 */
@RpcLite(serviceName = "userServiceImpl", timeout = 5)
public interface UserService {

  User createUser(String name, String address);

  Integer insertUsers(List<User> users);

  List getUserPets(Long userId);

  User getUserById(Long userId) throws UserNotFoundException;
}
