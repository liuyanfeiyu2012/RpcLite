package com.xxx.rpclite.client;

import com.xxx.rpclite.transport.client.NettyClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by klose on 2017/1/27.
 */
public class NettyClientPool {
  private static Logger logger = LoggerFactory.getLogger(NettyClientPool.class);

  /**
   * key: host:port
   * 发送到相同机器上的请求使用同一个NettyClient
   */
  private static Map<String, NettyClient> clientMap = new ConcurrentHashMap<>();

  private NettyClientPool() {
  }

  public static synchronized NettyClient createAndStartNettyClient(String address) {
    if (clientMap.containsKey(address)) {
      return clientMap.get(address);
    }
    String[] addr = address.split(":");
    NettyClient nettyClient = new NettyClient();
    nettyClient.start(addr[0], Integer.parseInt(addr[1]));
    clientMap.put(address, nettyClient);
    logger.debug("Create One Netty Client For Server: {}", address);
    try {
      //wait server to sync.
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      logger.error("Unexcepted Error e: {}.", e);
    }
    return nettyClient;
  }

}
