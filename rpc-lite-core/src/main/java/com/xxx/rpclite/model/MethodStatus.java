package com.xxx.rpclite.model;

/**
 * Created by klose on 2017/1/26.
 */
public enum MethodStatus {

  SUCCESS(200),

  FAILED(500);

  private int status;

  MethodStatus(int status) {
    this.status = status;
  }

  public int getStatus() {
    return status;
  }
}
