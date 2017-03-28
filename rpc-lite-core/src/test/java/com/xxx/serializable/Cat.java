package com.xxx.serializable;

import java.io.Serializable;

/**
 * Created by klose on 2017/2/17.
 */
public class Cat extends ExtendTest.Animal implements Serializable {
  private static final long serialVersionUID = -8940196742313994740L;

  public Integer set;

  public Integer getSet() {
    return set;
  }

  public void setSet(Integer set) {
    this.set = set;
  }

  public static long getSerialVersionUID() {
    return serialVersionUID;
  }
}
