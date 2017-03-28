package com.xxx.rpclite.server;

import com.xxx.rpclite.registry.RegisterService;
import com.xxx.rpclite.registry.entity.RegisterConfig;
import com.xxx.rpclite.registry.entity.RegisterInfo;
import com.xxx.rpclite.transport.server.NettyServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by klose on 2017/1/26.
 */
public class RpcServiceExporter extends NettyServer {
  private static Logger logger = LoggerFactory.getLogger(RpcServiceExporter.class);

  private int port;

  private int workThreads;

  private List<Object> registerServices;

  private RegisterConfig registerConfig;

  private RegisterService registerService;

  public RpcServiceExporter(){}

  public RpcServiceExporter(int port, int workThreads, List registerServices){
    new RpcServiceExporter(port, workThreads, registerServices, null);
  }

  public RpcServiceExporter(int port, int workThreads, List registerServices, RegisterConfig registerConfig) {
    this.port = port;
    this.workThreads = workThreads;
    this.registerServices = registerServices;
    this.registerConfig = registerConfig;
    init();
  }

  public void init(){
    Map map = createServiceImplMap(registerServices);
    ServiceImplMethodPool.addMethodsToCache(registerServices);
    try {
      super.start(port, workThreads, map);
    } catch (Exception e) {
      throw new RuntimeException("Rpc Server Start Exception ", e);
    }
    registerOnServiceRegisterCenter();
    logger.info("Rpc Service Exporter Start Success, Listen On Port: {}", port);
  }

  private void registerOnServiceRegisterCenter() {
    if (registerConfig == null) {
      return;
    }
    String clusterAddress = registerConfig.getClusterAddress();
    String zkNodePath = registerConfig.getZookeeperNodePath();
    String serviceName = registerConfig.getServiceName();
    String host = registerConfig.getHost();
    int port = registerConfig.getPort();

    RegisterInfo info = new RegisterInfo(serviceName, host, port);
    registerService = new RegisterService(clusterAddress, zkNodePath, info);
    registerService.init();
    logger.info("Service Register On Server " + host + " And Listen On Port " + port + ".");
  }

  private Map createServiceImplMap(List registerServices) {
    if (registerServices == null || registerServices.size() == 0) {
      throw new RuntimeException("Register Services Can Not Be Empty.");
    }
    Map serviceImpls = new HashMap<>();
    for (Object obj : registerServices) {
      String serviceName = generateServiceName(obj);
      serviceImpls.put(serviceName, obj);
    }
    return serviceImpls;
  }

  private String generateServiceName(Object obj) {
    return obj.getClass().getSimpleName().toLowerCase();
  }

  public void close(){
    super.close();
    if (registerService != null){
      registerService.destroy();
      registerService = null;
    }
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public int getWorkThreads() {
    return workThreads;
  }

  public void setWorkThreads(int workThreads) {
    this.workThreads = workThreads;
  }

  public List<Object> getRegisterServices() {
    return registerServices;
  }

  public void setRegisterServices(List<Object> registerServices) {
    this.registerServices = registerServices;
  }

  public RegisterConfig getRegisterConfig() {
    return registerConfig;
  }

  public void setRegisterConfig(RegisterConfig registerConfig) {
    this.registerConfig = registerConfig;
  }
}
