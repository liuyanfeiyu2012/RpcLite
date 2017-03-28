package com.xxx.rpclite.registry.entity;

/**
 * Created by klose on 2017/2/7.
 */
public class RegisterInfo {

  private static final String PREFIX = "PROVIDER";

  private String serviceName;

  private String host;

  private Integer port;

  public RegisterInfo(String serviceName, String host, int port){
    this.serviceName = serviceName;
    this.host = host;
    this.port = port;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj != null && obj instanceof RegisterInfo){
      RegisterInfo info = (RegisterInfo) obj;
      if (this.serviceName.equals(info.getServiceName())
          && this.host.equals(info.getHost())
          && this.port == info.getPort()){
        return true;
      }
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = this.serviceName.hashCode();
    result = 29 * result + host.hashCode();
    result = 29 * result + port.hashCode();
    return result;
  }

  public String getServiceName() {
    return serviceName;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  /**
   *
   * @return
   * e.g
   * PROVIDER1921681010-8080
   */
  public String toNodeName(){
    return PREFIX + host.replaceAll("\\.", "") + "-" + port;
  }

  public String toAddressName(){
    return host + ":" + port;
  }
}
