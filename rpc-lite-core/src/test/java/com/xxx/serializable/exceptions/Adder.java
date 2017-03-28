package com.xxx.serializable.exceptions;

/**
 * Created by klose on 2017/2/17.
 */
public class Adder {

  public int sum(int a, int b){
    return a + b;
  }

  public int sumWithException(int a, int b) throws Throwable{
    throw new Exception1("hhh");
  }
}
