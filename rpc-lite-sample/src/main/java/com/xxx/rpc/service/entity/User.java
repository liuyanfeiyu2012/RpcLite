package com.xxx.rpc.service.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by klose on 2017/1/27.
 */
public class User implements Serializable {

  private Long userId;

  private String name;

  private String address;

  private Date createDate;

  private List<Pet> pets;

  public User(){
    createDate = new Date();
  }

  public User(Long userId, String name, String address, List<Pet> pets) {
    this.userId = userId;
    this.name = name;
    this.address = address;
    this.pets = pets;
    createDate = new Date();
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<Pet> getPets() {
    return pets;
  }

  public void setPets(List<Pet> pets) {
    this.pets = pets;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }
}
