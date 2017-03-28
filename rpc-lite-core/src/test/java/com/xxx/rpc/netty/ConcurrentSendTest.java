package com.xxx.rpc.netty;

import com.xxx.rpclite.model.MethodCallBack;
import com.xxx.rpclite.model.MethodRequest;
import com.xxx.rpclite.model.MethodResponse;
import com.xxx.rpclite.client.MethodSenderAndCallbackHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import org.junit.Assert;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by klose on 2017/1/26.
 */
public class ConcurrentSendTest {

  static MethodSenderAndCallbackHandler methodSenderAndCallbackHandler = new MethodSenderAndCallbackHandler();

  @Test
  public void test() throws Exception {
    InetSocketAddress serverAddress = new InetSocketAddress("localhost", 8899);
    EventLoopGroup eventLoopGroup = new NioEventLoopGroup(2);
    Bootstrap bootstrap = new Bootstrap();

    bootstrap.group(eventLoopGroup)
        .channel(NioSocketChannel.class)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
            pipeline.addLast(new LengthFieldPrepender(4));
            pipeline.addLast(new ObjectEncoder());
            pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
            pipeline.addLast(methodSenderAndCallbackHandler);
          }
        })
        .option(ChannelOption.SO_KEEPALIVE, true);

    ChannelFuture channelFuture = bootstrap.connect(serverAddress);
    channelFuture.addListener(new ChannelFutureListener() {
      public void operationComplete(final ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
          MethodSenderAndCallbackHandler handler = channelFuture.channel().pipeline().get(MethodSenderAndCallbackHandler.class);
          methodSenderAndCallbackHandler = handler;
        }
      }
    });

    Thread.sleep(1000);

    System.out.println("==========test await.==========");

    List<Sender> senders = new ArrayList<>();
    int num = 100;
    for (int i = 0; i < num; i++) {
      senders.add(new Sender(methodSenderAndCallbackHandler, i));
    }

    for (Sender sender : senders) {
      sender.start();
    }

    for (Sender sender : senders) {
      sender.join();
    }

    System.out.println("===========test end.==========");
  }

  static class Sender extends Thread {

    int NO;

    MethodSenderAndCallbackHandler methodSenderAndCallbackHandler;

    public Sender(MethodSenderAndCallbackHandler methodSenderAndCallbackHandler, int no) {
      this.methodSenderAndCallbackHandler = methodSenderAndCallbackHandler;
      this.NO = no;
    }

    @Override
    public void run() {
      try {
        sleep(ThreadLocalRandom.current().nextInt(0, 1000));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      MethodRequest request = new MethodRequest();
      request.setId(UUID.randomUUID().toString());
      request.setServiceName("serviceName");
      request.setMethodName("methodName");
//      request.setParameterTypes(null);
      request.setParameterValues(null);
      MethodCallBack callBack = methodSenderAndCallbackHandler.sendRequest(request);
      try {
        Object response = callBack.await();
        MethodResponse result = (MethodResponse) response;
        Assert.assertTrue(request.getId().equals(result.getId()));
        System.out.println("request send succeed, NO : " + NO);
      } catch (Exception e) {
        e.printStackTrace();
        System.err.println("request send error, NO : " + NO);
      }
    }
  }


}
