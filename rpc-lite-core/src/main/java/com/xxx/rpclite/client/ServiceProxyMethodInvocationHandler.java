package com.xxx.rpclite.client;

import com.xxx.rpclite.annotation.RpcLite;
import com.xxx.rpclite.exceptions.ClientException;
import com.xxx.rpclite.model.MethodCallBack;
import com.xxx.rpclite.model.MethodRequest;
import com.xxx.rpclite.model.MethodResponse;
import com.xxx.rpclite.model.MethodStatus;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by klose on 2017/1/27.
 */
public class ServiceProxyMethodInvocationHandler implements InvocationHandler, Serializable {
  private static Logger logger = LoggerFactory.getLogger(ServiceProxyMethodInvocationHandler.class);

  private Map<String, MethodRequest> methodRequestTemplateMap = new ConcurrentHashMap<>();

  private Class serviceInterface;

  public ServiceProxyMethodInvocationHandler(Class serviceInterface) {
    this.serviceInterface = serviceInterface;
    addMethodRequestToCache(serviceInterface);
    logger.debug("Create Proxy For Service, Method Request Size: {}", methodRequestTemplateMap.size());
  }

  private void addMethodRequestToCache(Class clazz) {
    String clazzName = clazz.getSimpleName();
    Method[] methods = clazz.getDeclaredMethods();
    for (Method method : methods) {
      String methodName = method.getName();
      String signature = clazzName + ":" + methodName;
      String serviceName;
      int timeout = 0;
      int retry;
      methodName = method.getName();
      serviceName = clazz.getSimpleName().toLowerCase();
      Annotation clazzAnnotation = clazz.getAnnotation(RpcLite.class);
      if (clazzAnnotation != null) {
        RpcLite clazzRpcLite = (RpcLite) clazzAnnotation;
        if (StringUtils.isNotEmpty(clazzRpcLite.serviceName())) {
          serviceName = clazzRpcLite.serviceName().toLowerCase();
        }
        timeout = clazzRpcLite.timeout();
      }
      Annotation methodAnnotation = method.getAnnotation(RpcLite.class);
      if (methodAnnotation != null) {
        RpcLite methodRpcLite = (RpcLite) methodAnnotation;
        if (StringUtils.isNoneEmpty(methodRpcLite.serviceName())) {
          serviceName = methodRpcLite.serviceName().toLowerCase();
        }
        if (StringUtils.isNotEmpty(methodRpcLite.methodName())) {
          methodName = methodRpcLite.methodName();
        }
        if (methodRpcLite.timeout() != 0) {
          timeout = methodRpcLite.timeout();
        }
        if (methodRpcLite.retry() < 0) {
          retry = 1;
        } else {
          retry = methodRpcLite.retry();
        }
      }else {
        retry = 1;
      }

      MethodRequest request = new MethodRequest();
      request.setServiceName(serviceName);
      request.setMethodName(methodName);
      request.setTimeout(timeout);
      request.setRetry(retry);

      methodRequestTemplateMap.put(signature, request);
    }
  }


  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    String clazzName = method.getDeclaringClass().getSimpleName();
    String methodName = method.getName();
    String signature = clazzName + ":" + methodName;
    MethodRequest requestTemplate = methodRequestTemplateMap.get(signature);
    if (requestTemplate == null) {
      return null;
    }
    MethodRequest request = (MethodRequest) requestTemplate.clone();
    request.setId(UUID.randomUUID().toString());
    request.setParameterValues(args);

    return doSendRequest(request, method, 0);
  }

  private Object doSendRequest(MethodRequest request, Method method, int count) throws Throwable {
    logger.debug("Do Send Method Request, Id: {}, service: {}, method: {}, retry: {}, count: {}",
        request.getId(), request.getServiceName(), request.getMethodName(), request.getRetry(), count);
    int retry = request.getRetry();
    for (int i = count; i <= retry; i++) {
      MethodSenderAndCallbackHandler methodSender = MethodSenderAndCallbackHandlerManager
          .getMethodSenderAndCallbackHandlerByRoundRobin();
      if (methodSender == null) {
        throw new ClientException("NO Active Channel Found, May Be Lost Connection.");
      }
      MethodCallBack callBack = methodSender.sendRequest(request);
      MethodResponse response = callBack.await();
      if (response != null && response.getStatus() == MethodStatus.SUCCESS.getStatus()) {
        logger.debug("Do Receive Response Success, Id: {}", response.getId());
        return response.getValue();
      } else {
        if (isDeclaredException(method, response)) {
          throw response.getException().getCause();
        } else if (count == retry) {
          throw new ClientException("Can Not Get Response Till Timeout And Retry For " + retry +
              " Times.");
        } else {
          return doSendRequest(request, method, ++count);
        }
      }
    }
    return null;
  }

  private boolean isDeclaredException(Method method, MethodResponse response) {
    Class<?>[] types = method.getExceptionTypes();
    for (Class clazz : types) {
      Throwable throwable = response.getException().getCause();
      if (throwable.getClass().equals(clazz)) {
        return true;
      }
    }
    return false;
  }

}
