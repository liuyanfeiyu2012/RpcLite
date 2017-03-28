package com.xxx.rpclite.registry.entity;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by klose on 2017/2/7.
 */
public class RegisterConfig {

  private String clusterAddress;

  private String zookeeperNodePath;

  private String serviceName;

  private String host;

  private Integer port;

  public RegisterConfig(){}

  public RegisterConfig(String serviceName, String host, Integer port, String clusterAddress,
                        String zookeeperNodePath) {
    if (StringUtils.isEmpty(serviceName) || StringUtils.isEmpty(host) || port == null ||
        StringUtils.isEmpty(clusterAddress) || StringUtils.isEmpty(zookeeperNodePath)){
      throw new RuntimeException("Parameters Can Not Be Null Value.");
    }
    this.serviceName = serviceName;
    this.host = host;
    this.port = port;
    this.clusterAddress = clusterAddress;
    this.zookeeperNodePath = zookeeperNodePath;
  }

  public String getZookeeperNodePath() {
    return zookeeperNodePath;
  }

  public void setZookeeperNodePath(String zookeeperNodePath) {
    this.zookeeperNodePath = zookeeperNodePath;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public void setClusterAddress(String clusterAddress) {
    this.clusterAddress = clusterAddress;
  }

  public String getServiceName() {
    return serviceName;
  }

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

  public String getClusterAddress() {
    return clusterAddress;
  }
}
