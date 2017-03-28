package com.xxx.rpclite.server;


import com.xxx.rpclite.exceptions.ServerException;
import com.xxx.rpclite.model.MethodRequest;
import com.xxx.rpclite.model.MethodResponse;
import com.xxx.rpclite.model.MethodStatus;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by klose on 2017/1/27.
 */
public class MethodReceiveAndInvokerHandler extends ChannelInboundHandlerAdapter {
  private static Logger logger = LoggerFactory.getLogger(MethodReceiveAndInvokerHandler.class);

  private static Map<String, Object> serviceImplMap;

  private ExecutorService executor;

  public MethodReceiveAndInvokerHandler(int workerThreads, Map<String, Object> serviceImplMap) {
    MethodReceiveAndInvokerHandler.serviceImplMap = serviceImplMap;
    executor = Executors.newFixedThreadPool(workerThreads);
  }

  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    MethodRequest request = (MethodRequest) msg;
    logger.debug("Get Msg, messageId: {}, service: {}, method: {}", request.getId(), request
        .getServiceName(), request.getMethodName());
    executor.execute(new MethodProcessor(ctx, request));
  }

  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    ctx.close();
  }

  static class MethodProcessor implements Runnable {
    ChannelHandlerContext ctx;
    MethodRequest request;

    MethodProcessor(ChannelHandlerContext ctx, MethodRequest request) {
      this.ctx = ctx;
      this.request = request;
    }

    @Override
    public void run() {
      String serviceName = request.getServiceName();
      String methodName = request.getMethodName();
      Object[] parameterValues = request.getParameterValues();
      logger.debug("Process Message, messageId: {}, service: {}, method: {}", request.getId(),
          request.getServiceName(), request.getMethodName());
      Object impl = serviceImplMap.get(serviceName);
      MethodResponse response = new MethodResponse();
      response.setId(request.getId());
      try {
        if (impl == null) {
          logger.error("No Qualified " + serviceName + " Service Found For Message: {}", request.getId());
          throw new ServerException("No Qualified " + serviceName + " Service Found.");
        }

        String methodSignature = serviceName+":"+methodName;
        Method method = ServiceImplMethodPool.get(methodSignature);
        if (method == null) {
          logger.error("No Qualified " + methodName + " method Found For Message: {}", request.getId());
          throw new ServerException("No Qualified " + methodName + " Method Found.");
        }
        Object result = method.invoke(impl, parameterValues);
        response.setValue(result);
        response.setStatus(MethodStatus.SUCCESS.getStatus());
      } catch (Exception e) {
        if (e instanceof ServerException) {
          ServerException serverException = (ServerException) e;
          response.setErrorMsg(serverException.getErrorMsg());
          response.setStatus(serverException.getErrorCode());
        } else {
          response.setStatus(MethodStatus.FAILED.getStatus());
          response.setErrorMsg(e.getMessage());
          response.setException(e);
        }
      }
      logger.debug("Process Message Success, messageId: {}, service: {}, method: {}", request.getId(),
          request.getServiceName(), request.getMethodName());
      ctx.writeAndFlush(response);
    }
  }
}

