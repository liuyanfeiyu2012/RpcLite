package com.xxx.rpclite.client;

import com.xxx.rpclite.exceptions.ClientException;
import com.xxx.rpclite.registry.entity.DiscoverConfig;
import com.xxx.rpclite.utils.AddressUtil;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * Created by klose on 2017/1/27.
 */
public class RpcServiceProxyBeanFactory {
  private static Logger logger = LoggerFactory.getLogger(RpcServiceProxyBeanFactory.class);

  public RpcServiceProxyBeanFactory() {
  }

  public static <T> T getBean(Class<T> clazz, RpcServiceProxyBeanFactoryOption option) {
    logger.debug("Get Bean, Clazz: {}.", clazz.getSimpleName());
    return (T) RpcServiceProxyBeanFactory.proxy(clazz, option);
  }

  private static Object proxy(Class serviceInterface, RpcServiceProxyBeanFactoryOption
      rpcServiceProxyBeanFactoryOption) {
    if (serviceInterface == null) {
      throw new ClientException("Service Interface Can Not Be Empty.");
    }
    DiscoverConfig discoverConfig = rpcServiceProxyBeanFactoryOption.getDiscoverConfig();
    String remoteAddress = rpcServiceProxyBeanFactoryOption.getRemoteAddress();
    if (discoverConfig != null) {
      createLBNettyClient(discoverConfig);
    } else if (StringUtils.isNotEmpty(remoteAddress)) {
      createDefaultNettyClient(remoteAddress);
    } else {
      throw new ClientException("Missing Discover Config Or Remote Address.");
    }
    ServiceProxyMethodInvocationHandler proxy = new ServiceProxyMethodInvocationHandler(serviceInterface);
    return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new
        Class<?>[]{serviceInterface}, proxy);
  }

  private static void createLBNettyClient(DiscoverConfig discoverConfig) {
    String serviceName = discoverConfig.getServiceName();
    String nodePath = discoverConfig.getDiscoverNodePath();
    String clusterAddress = discoverConfig.getClusterAddress();

    LBNettyClientManager.createLBNettyClient(serviceName, nodePath, clusterAddress);
    logger.debug("Create LB Netty Client, ServiceName: {}, nodePath: {}, clusterAddress: {}",
        serviceName, nodePath, clusterAddress);
  }

  private static void createDefaultNettyClient(String remoteAddress) {
    String[] hosts = AddressUtil.fromatAddress(remoteAddress);
    if (hosts != null && hosts.length != 0) {
      for (String host : hosts) {
        NettyClientPool.createAndStartNettyClient(host);
      }
    } else {
      throw new ClientException("Remote Address Is Invalid.");
    }
    logger.debug("Create Default Netty Client For Server, remoteAddress: {}", remoteAddress);
  }

}
