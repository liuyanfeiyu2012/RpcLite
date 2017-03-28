package com.xxx.rpclite.exceptions;

/**
 * Created by klose on 2017/1/27.
 */
public class ClientException extends RuntimeException {

  private Integer errorCode;

  private String errorMsg;

  public ClientException(String errorMsg){
    super(errorMsg);
    this.errorMsg = errorMsg;
  }

  public ClientException(Integer errorCode, String errorMsg) {
    super(errorMsg);
    this.errorCode = errorCode;
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
