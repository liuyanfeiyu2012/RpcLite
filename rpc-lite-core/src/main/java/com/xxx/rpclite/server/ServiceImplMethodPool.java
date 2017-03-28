package com.xxx.rpclite.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by klose on 2017/1/29.
 */
public class ServiceImplMethodPool {
  private static Logger logger = LoggerFactory.getLogger(ServiceImplMethodPool.class);

  private static Map<String, Method> methodMap = new ConcurrentHashMap<>();

  private ServiceImplMethodPool() {
  }

  public static void put(String methodSignature, Method method) {
    if (methodMap.containsKey(methodSignature)) {
//      throw new RuntimeException(methodSignature + " method already exist.");
    }
    methodMap.put(methodSignature, method);
  }

  public static Method get(String methodSignature) {
    return methodMap.get(methodSignature);
  }

  public static void addMethodsToCache(List registerServices) {
    for (Object impl : registerServices) {
      String serviceName = impl.getClass().getSimpleName().toLowerCase();
      Method[] methods = impl.getClass().getDeclaredMethods();
      for (Method method : methods) {
        String methodName = method.getName();
        String signature = serviceName + ":" + methodName;
        put(signature, method);
      }
    }
    logger.info("Put Service Impl Methods Into Cache, Cache Size: {}", methodMap.size());
  }
}
