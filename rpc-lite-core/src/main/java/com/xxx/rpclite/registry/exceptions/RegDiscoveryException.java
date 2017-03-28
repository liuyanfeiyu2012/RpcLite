package com.xxx.rpclite.registry.exceptions;

public class RegDiscoveryException extends RuntimeException {

  public RegDiscoveryException(String message) {
    super(message);
  }

  public RegDiscoveryException(String message, Throwable cause) {
    super(message, cause);
  }

  public RegDiscoveryException(Exception e) {
    super(e);
  }

}