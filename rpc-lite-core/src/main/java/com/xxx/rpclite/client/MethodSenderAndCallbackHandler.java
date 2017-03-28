package com.xxx.rpclite.client;

import com.xxx.rpclite.model.MethodCallBack;
import com.xxx.rpclite.model.MethodRequest;
import com.xxx.rpclite.model.MethodResponse;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;

public class MethodSenderAndCallbackHandler extends ChannelInboundHandlerAdapter {
  private static Logger logger = LoggerFactory.getLogger(MethodSenderAndCallbackHandler.class);

  private static ConcurrentHashMap<String, MethodCallBack> callBackMap = new ConcurrentHashMap<>();

  private volatile Channel channel;

  public MethodSenderAndCallbackHandler(){}

  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    logger.debug("Channel Active.");
    super.channelActive(ctx);
  }

  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    super.channelRegistered(ctx);
    this.channel = ctx.channel();
  }

  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    MethodResponse response = (MethodResponse) msg;
    String messageId = response.getId();
    logger.debug("Received Msg, messageId: {}.", messageId);
    MethodCallBack callBack = callBackMap.get(messageId);
    if (callBack != null) {
      callBackMap.remove(messageId);
      callBack.signal(response);
    }
  }

  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    logger.error("Exception Caught, cause: {}", cause);
    ctx.close();
  }

  public void close() {
    channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  public MethodCallBack sendRequest(MethodRequest request) {
    MethodCallBack callBack = new MethodCallBack(request);
    callBackMap.put(request.getId(), callBack);
    channel.writeAndFlush(request);
    logger.debug("Send Request, requestId: {}, serviceName: {}, method: {}", request.getId(),
        request.getServiceName(), request.getMethodName());
    return callBack;
  }
}
