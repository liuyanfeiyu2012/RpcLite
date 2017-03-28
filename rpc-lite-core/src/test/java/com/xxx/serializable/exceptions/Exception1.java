package com.xxx.serializable.exceptions;

/**
 * Created by klose on 2017/2/17.
 */
public class Exception1 extends Exception{

  private String msg;

  private Integer errorCode;

  public Exception1(){
  }

  public Exception1(String msg){
    super(msg);
  }

  public Exception1(String msg, Throwable throwable){
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
