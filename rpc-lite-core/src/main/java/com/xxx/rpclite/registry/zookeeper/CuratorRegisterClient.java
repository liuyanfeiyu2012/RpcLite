package com.xxx.rpclite.registry.zookeeper;


import com.xxx.rpclite.registry.exceptions.RegDiscoveryException;
import com.xxx.rpclite.registry.entity.RegisterInfo;

import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;


public class CuratorRegisterClient {
  private static Logger logger = LoggerFactory.getLogger(CuratorRegisterClient.class);

  private static final int DEFAULT_SESSION_TIMEOUT = 15000;

  private static final int DEFAULT_CONNECTION_TIMEOUT = 30000;

  private static final String SEPARATOR = "/";

  private String registerRootPath;

  private HashSet<RegisterInfo> registerInfos;

  private CuratorFramework client;

  protected void init(String clusterAddress, String registerRootPath, HashSet<RegisterInfo>
      registerInfos) {
    synchronized (CuratorRegisterClient.class){
      this.registerRootPath = registerRootPath;
      this.registerInfos = registerInfos;
      initConnection(clusterAddress);
      createTempNode(registerRootPath, registerInfos);
    }
  }

  private void createTempNode(String registerRootPath, HashSet<RegisterInfo> registerInfos) {
    for (RegisterInfo info : registerInfos) {
      String path = registerRootPath + SEPARATOR + info.getServiceName() + SEPARATOR + info.toNodeName();
      String content = info.toAddressName();
      try {
        if (null == client.checkExists().forPath(path)){
          client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, content.getBytes("UTF-8"));
        }
      } catch (Exception e) {
        //should never happen.
        logger.error("Create TempNode Error, error:{}", e);
      }
    }
    logger.debug("Create TempNode OK.");
  }

  protected void initConnection(String connectString) {
    if (StringUtils.isEmpty(connectString)) {
      throw new RegDiscoveryException("Missing Connection String Or RegisterRootPath.");
    }
    try {
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(DEFAULT_SESSION_TIMEOUT, 3);
      client = CuratorFrameworkFactory.builder()
          .connectString(connectString)
          .retryPolicy(retryPolicy)
          .connectionTimeoutMs(DEFAULT_CONNECTION_TIMEOUT)
          .build();
      client.start();

      client.getConnectionStateListenable().addListener(new ConnectionStateListener() {
        @Override
        public void stateChanged(CuratorFramework client, ConnectionState newState) {
          switch (newState){
            case CONNECTED:
              logger.debug("CuratorFramework Connection CONNECTED.");
              break;
            case LOST:
              logger.error("CuratorFramework Connection LOST.");
              break;
            case SUSPENDED:
              logger.error("CuratorFramework Connection SUSPENDED.");
              break;
            case RECONNECTED:
              logger.debug("CuratorFramework Connection RECONNECTED");
              new Thread(new Runnable() {
                @Override
                public void run() {
                  createTempNode(registerRootPath, registerInfos);
                }
              }).start();
              break;
          }
        }
      });
    } catch (Exception e) {
      logger.error("CuratorRegisterClient Initial Connection Exception, exception = {}", e);
    }
    logger.debug("CuratorRegisterClient Init Connection OK.");
  }

  protected void destroy() {
    logger.debug("Destroy CuratorRegisterClient ...");
    if (client != null && client.getState().equals(CuratorFrameworkState.STARTED)) {
      client.close();
      client = null;
    }
    logger.info("Destroy CuratorRegisterClient OK!");
  }

}