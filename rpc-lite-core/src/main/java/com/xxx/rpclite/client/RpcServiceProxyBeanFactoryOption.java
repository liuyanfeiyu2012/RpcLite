package com.xxx.rpclite.client;

import com.xxx.rpclite.registry.entity.DiscoverConfig;

/**
 * Created by klose on 2017/2/21.
 */
public class RpcServiceProxyBeanFactoryOption {

  private String remoteAddress;

  private DiscoverConfig discoverConfig;

  public String getRemoteAddress() {
    return remoteAddress;
  }

  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }

  public DiscoverConfig getDiscoverConfig() {
    return discoverConfig;
  }

  public void setDiscoverConfig(DiscoverConfig discoverConfig) {
    this.discoverConfig = discoverConfig;
  }
}
