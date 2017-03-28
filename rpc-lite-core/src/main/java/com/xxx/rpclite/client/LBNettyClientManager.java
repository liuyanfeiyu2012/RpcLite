package com.xxx.rpclite.client;

import com.xxx.rpclite.registry.DiscoverService;
import com.xxx.rpclite.transport.client.LBNettyClient;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by klose on 2017/2/8.
 */
public class LBNettyClientManager {
  private static Logger logger = LoggerFactory.getLogger(LBNettyClientManager.class);

  private static Map<String, LBNettyClient> clientMap = new ConcurrentHashMap<>();

  private static DiscoverService discoverService;

  //只有启动时候创建一次
  private static volatile boolean isCreated = false;

  public static synchronized void createLBNettyClient(String serviceName, String nodePath, String
      clusterAddress) {
    if (isCreated){
      return;
    }
    addListener(serviceName, nodePath, clusterAddress);

    List<String> hosts = discoverService.getHosts();
    logger.info("Available hosts size: {}.", hosts.size());
    for (String host : hosts) {
      createLBNettyClient(host);
    }

    try {
      Thread.sleep(1500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    isCreated = true;
  }

  private static void addListener(String serviceName, String nodePath, String
      clusterAddress) {
    discoverService = new DiscoverService(clusterAddress, nodePath, serviceName) {
      @Override
      public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) {
        ChildData data = event.getData();
        if (data == null || data.getData() == null) {
          return;
        }
        String path = data.getPath();
        String nodeName = formatFullPathToNodeName(path);
        String serviceName = formatFullPathToServiceName(path);
        String nodeInfo = new String(data.getData(), UTF8);
        PathChildrenCacheEvent.Type type = event.getType();
        switch (type) {
          case CHILD_ADDED:
            logger.info("One Node Added, nodeName:{}, serviceName: {}", nodeName, serviceName);
            add(nodeInfo);
            break;
          case CHILD_UPDATED:
            break;
          case CHILD_REMOVED:
            logger.info("One Node Removed, nodeName:{}, serviceName: {}", nodeName, serviceName);
            remove(nodeInfo);
            break;
          case CONNECTION_LOST:
            logger.error("CuratorFramework Connection CONNECTION_LOST.");
            break;
          case CONNECTION_RECONNECTED:
            break;
        }
      }
    };
    discoverService.init();
  }

  private static void createLBNettyClient(String host) {
    if (!clientMap.containsKey(host)) {
      String[] addr = host.split(":");
      LBNettyClient nettyClient = new LBNettyClient();
      nettyClient.start(addr[0], Integer.parseInt(addr[1]));
      clientMap.put(host, nettyClient);
    }
  }

  public static void add(String host) {
    createLBNettyClient(host);
  }

  public static void remove(String host) {
    clientMap.remove(host);
  }

  public static void close() {
    discoverService.destroy();
    discoverService = null;
    for (LBNettyClient client : clientMap.values()) {
      client.close();
    }
  }
}
