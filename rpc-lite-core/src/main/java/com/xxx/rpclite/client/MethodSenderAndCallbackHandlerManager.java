package com.xxx.rpclite.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by klose on 2017/1/30.
 */
public class MethodSenderAndCallbackHandlerManager {
  private static Logger logger = LoggerFactory.getLogger(MethodSenderAndCallbackHandlerManager.class);

  private static AtomicLong callCount = new AtomicLong(0);

  private static List<MethodSenderAndCallbackHandler> methodSenderAndCallbackHandlers = new CopyOnWriteArrayList<>();

  public static void put(MethodSenderAndCallbackHandler methodSenderAndCallbackHandler) {
    methodSenderAndCallbackHandlers.add(methodSenderAndCallbackHandler);
    logger.info("Cache One MethodSender, Cache Size: {}", methodSenderAndCallbackHandlers.size());
  }

  public static void remove(MethodSenderAndCallbackHandler methodSenderAndCallbackHandler) {
    methodSenderAndCallbackHandlers.remove(methodSenderAndCallbackHandler);
    logger.info("Remove One MethodSender, Cache Size: {}", methodSenderAndCallbackHandlers.size());
  }

  public static MethodSenderAndCallbackHandler getMethodSenderAndCallbackHandlerByRoundRobin() {
    int size = methodSenderAndCallbackHandlers.size();
    if (size == 0){
      return null;
    }
    if (size == 1) {
      logger.debug("Use The First Method Sender.");
      return methodSenderAndCallbackHandlers.get(0);
    }
    int mod = (int) (callCount.incrementAndGet() % size);
    logger.debug("Select The Index: {} Method Sender.", mod);
    return methodSenderAndCallbackHandlers.get(mod);
  }
}
