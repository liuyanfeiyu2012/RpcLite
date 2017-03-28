package com.xxx.rpclite.registry;

import com.xxx.rpclite.registry.exceptions.RegDiscoveryException;
import com.xxx.rpclite.registry.zookeeper.CuratorDiscoverClient;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;

/**
 * Created by klose on 2017/2/7.
 */
public abstract class DiscoverService extends CuratorDiscoverClient {

  private String clusterAddress;

  private String discoverPath;

  private String serviceName;

  public DiscoverService(String clusterAddress, String discoverPath, String serviceName) {
    if (StringUtils.isEmpty(clusterAddress) || StringUtils.isEmpty(discoverPath)) {
      throw new RegDiscoveryException("Illegal Connect String Or Discover Path");
    }
    this.clusterAddress = clusterAddress;
    this.discoverPath = discoverPath;
    this.serviceName = serviceName;
    if (StringUtils.isEmpty(serviceName)) {
      throw new RegDiscoveryException("Service Names Can Not Be Empty.");
    }
  }

  public abstract void childEvent(CuratorFramework client, PathChildrenCacheEvent event);

  public void init() {
    super.init(clusterAddress, discoverPath, serviceName);
  }

  public void destroy() {
    super.destroy();
  }

}
