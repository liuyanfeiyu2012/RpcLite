package com.xxx.rpclite.model;

import java.io.Serializable;

/**
 * Created by klose on 2017/1/27.
 */
public class MethodResponse implements Serializable {

  private String id;

  private Object value;

  private Integer status = MethodStatus.SUCCESS.getStatus();

  private String errorMsg;

  private Exception exception;

  public MethodResponse(){}

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Object getValue() {
    return value;
  }

  public void setValue(Object value) {
    this.value = value;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Exception getException() {
    return exception;
  }

  public void setException(Exception exception) {
    this.exception = exception;
  }
}

