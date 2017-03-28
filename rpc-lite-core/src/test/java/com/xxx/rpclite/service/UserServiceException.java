package com.xxx.rpclite.service;

/**
 * Created by klose on 2017/2/20.
 */
public class UserServiceException extends Exception {

  private String msg;

  private Integer errorCode;

  public UserServiceException() {}

  public UserServiceException(String msg) {
    super(msg);
  }

  public UserServiceException(String msg, Throwable throwable) {
    super(msg, throwable);
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public Integer getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(Integer errorCode) {
    this.errorCode = errorCode;
  }

}
