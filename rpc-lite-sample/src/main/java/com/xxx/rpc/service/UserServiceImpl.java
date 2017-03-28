package com.xxx.rpc.service;


import com.xxx.rpc.service.entity.Pet;
import com.xxx.rpc.service.entity.User;
import com.xxx.rpc.service.entity.UserNotFoundException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by klose on 2017/1/27.
 */
public class UserServiceImpl implements UserService {

  @Override
  public User createUser(String name, String address) {
    User user = new User();
    user.setName(name);
    user.setAddress(address);
    user.setUserId(1l);
    return user;
  }

  @Override
  public Integer insertUsers(List<User> users) {
    users.forEach(user -> {
      System.out.println("name: " + user.getName() + ", address: " + user.getAddress());
    });
    return users.size();
  }

  @Override
  public List<Pet> getUserPets(Long userId) {
    Pet pet1 = new Pet(userId, "Cat", "喵喵", 2);
    Pet pet2 = new Pet(userId, "Dog", "大黄", 1);
    Pet pet3 = new Pet(userId, "Tortoise", "龟龟", 100);
    return Arrays.asList(pet1, pet2, pet3);
  }

  @Override
  public User getUserById(Long userId) throws UserNotFoundException {
    if (userId % 2 != 0) {
      UserNotFoundException ex = new UserNotFoundException();
      ex.setErrorCode(404);
      ex.setErrorMsg("User Not Found.");
      throw ex;
    } else {
      throw new RuntimeException("Other UnChecked Exception.");
    }
  }
}
