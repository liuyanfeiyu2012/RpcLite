package com.xxx.rpclite.registry.entity;

/**
 * Created by klose on 2017/2/8.
 */
public class DiscoverConfig {

  private String clusterAddress;

  private String serviceName;

  private String discoverNodePath;

  public DiscoverConfig(){}

  public DiscoverConfig(String clusterAddress, String serviceName, String discoverNodePath) {
    this.clusterAddress = clusterAddress;
    this.serviceName = serviceName;
    this.discoverNodePath = discoverNodePath;
  }

  public String getClusterAddress() {
    return clusterAddress;
  }

  public void setClusterAddress(String clusterAddress) {
    this.clusterAddress = clusterAddress;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public String getDiscoverNodePath() {
    return discoverNodePath;
  }

  public void setDiscoverNodePath(String discoverNodePath) {
    this.discoverNodePath = discoverNodePath;
  }
}
