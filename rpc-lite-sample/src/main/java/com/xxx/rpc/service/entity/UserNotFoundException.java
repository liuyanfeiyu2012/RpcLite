package com.xxx.rpc.service.entity;

/**
 * Created by klose on 2017/2/27.
 */
public class UserNotFoundException extends Exception {

  private String errorMsg;

  private Integer errorCode;

  public UserNotFoundException(){}

  public UserNotFoundException(String errorMsg, Integer errorCode) {
    this.errorMsg = errorMsg;
    this.errorCode = errorCode;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Integer getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(Integer errorCode) {
    this.errorCode = errorCode;
  }


}
