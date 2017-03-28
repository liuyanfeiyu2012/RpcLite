package com.xxx.rpc.service.entity;

import java.io.Serializable;

/**
 * Created by klose on 2017/2/24.
 */
public class Pet implements Serializable {

  private Long ownerId;

  private String category;

  private String name;

  private Integer age;

  public Pet() {
  }

  public Pet(Long ownerId, String category, String name, Integer age) {
    this.ownerId = ownerId;
    this.category = category;
    this.name = name;
    this.age = age;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Long getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(Long ownerId) {
    this.ownerId = ownerId;
  }

  @Override
  public String toString() {
    return "ownerId: " + ownerId + ", category: " + category + ", name: " + name + ", age: " + age
        + ".";
  }
}
