package com.xxx.rpclite.exceptions;

import com.xxx.rpclite.model.MethodStatus;

/**
 * Created by klose on 2017/1/27.
 */
public class ServerException extends RuntimeException {


  private Integer errorCode;

  private String errorMsg;

  public ServerException(String errorMsg){
    super(errorMsg);
    this.errorMsg = errorMsg;
  }

  public ServerException(MethodStatus status, String errorMsg) {
    super(errorMsg);
    this.errorCode = status.getStatus();
    this.errorMsg = errorMsg;
  }

  public Integer getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(Integer errorCode) {
    this.errorCode = errorCode;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }
}
