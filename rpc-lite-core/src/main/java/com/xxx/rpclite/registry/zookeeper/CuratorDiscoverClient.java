package com.xxx.rpclite.registry.zookeeper;

import com.xxx.rpclite.registry.exceptions.RegDiscoveryException;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class CuratorDiscoverClient implements PathChildrenCacheListener {
  private static Logger logger = LoggerFactory.getLogger(CuratorDiscoverClient.class);
  private static final int DEFAULT_SESSION_TIMEOUT = 15000;

  private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;

  private static final String SEPARATOR = "/";

  public final Charset UTF8 = Charset.forName("UTF-8");

  private String discoverRootPath;

  private CuratorFramework client;

  private PathChildrenCache pathChildrenCache;

  private List<String> hosts = new ArrayList<>();

  protected void init(String clusterAddress, String discoverRootPath, String serviceName) {
    synchronized (CuratorDiscoverClient.class) {
      this.discoverRootPath = discoverRootPath;
      initConnection(clusterAddress);
      initCaches(discoverRootPath, serviceName);
      loadDataFromCache();
    }
  }

  private void loadDataFromCache() {
      List<ChildData> children = pathChildrenCache.getCurrentData();
      for (ChildData child : children) {
        if (child.getData() == null) {
          continue;
        }
        String nodeInfo = new String(child.getData(), UTF8);
        hosts.add(nodeInfo);
      }
  }


  private void initCaches(String discoverRootPath, String serviceName) {
    pathChildrenCache = new PathChildrenCache(client, discoverRootPath + SEPARATOR + serviceName, true);
    pathChildrenCache.getListenable().addListener(this);
    try {
      pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
    } catch (Exception e) {
      logger.error("CuratorDiscoverClient Init Caches Exception, exception = {}", e);
    }
  }


  private void initConnection(String connectString) {
    if (StringUtils.isEmpty(connectString)) {
      throw new RegDiscoveryException("Missing Connection String.");
    }
    try {
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(DEFAULT_SESSION_TIMEOUT, 3);
      client = CuratorFrameworkFactory.builder()
          .connectString(connectString)
          .retryPolicy(retryPolicy)
          .connectionTimeoutMs(DEFAULT_CONNECTION_TIMEOUT)
          .build();
      client.start();
    } catch (Exception e) {
      logger.error("CuratorDiscoverClient Init Connection Exception, exception = {}", e);
    }
    logger.debug("InitConnection OK.");
  }

  protected void destroy() {
    logger.debug("Destroy CuratorDiscoverClient ...");
    try {
      pathChildrenCache.close();
    } catch (IOException e) {
      pathChildrenCache = null;
    }
    pathChildrenCache = null;
    if (client != null && client.getState().equals(CuratorFrameworkState.STARTED)) {
      try {
        client.close();
      } catch (Exception e) {
        client = null;
      }
      client = null;
    }
    logger.debug("Destroy CuratorDiscoverClient OK!");
  }

  @Override
  public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
  }

   public String formatFullPathToNodeName(String fullPath) {
    return fullPath.substring(fullPath.lastIndexOf(SEPARATOR) + 1, fullPath.length());
  }

   public String formatFullPathToServiceName(String fullPath) {
    fullPath = fullPath.replaceAll(discoverRootPath + SEPARATOR, "");
    return fullPath.substring(0, fullPath.lastIndexOf(SEPARATOR));
  }

  public List<String> getHosts() {
    return hosts;
  }
}