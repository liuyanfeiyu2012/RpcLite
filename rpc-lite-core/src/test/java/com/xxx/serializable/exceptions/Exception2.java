package com.xxx.serializable.exceptions;

/**
 * Created by klose on 2017/2/17.
 */
public class Exception2 extends RuntimeException{

  private String msg;

  private Integer errorCode;

  public Exception2(String msg){
    super(msg);
  }

  public Exception2(String msg, Throwable throwable){
    super(msg, throwable);
  }
}
