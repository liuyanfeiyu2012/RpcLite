package com.xxx.rpclite.transport.client;

import com.xxx.rpclite.client.LBNettyClientManager;
import com.xxx.rpclite.client.MethodSenderAndCallbackHandler;
import com.xxx.rpclite.client.MethodSenderAndCallbackHandlerManager;
import com.xxx.rpclite.transport.kryo.KryoDecoder;
import com.xxx.rpclite.transport.kryo.KryoEncoder;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

/**
 * Created by klose on 2017/1/26.
 */
public class LBNettyClient {
  private static Logger logger = LoggerFactory.getLogger(LBNettyClient.class);
  private static int AVAILABLE_PROCESSOR_NUM = Runtime.getRuntime().availableProcessors();


  private String host;
  private int port;
  private volatile MethodSenderAndCallbackHandler methodSenderAndCallbackHandler;
  private InetSocketAddress serverAddress;
  private volatile EventLoopGroup eventLoopGroup;
  private volatile Bootstrap bootstrap;

  private volatile int retryCount = 0;
  private volatile int retryInterval = 3;


  public void start(String host, int port) {
    this.host = host;
    this.port = port;
    serverAddress = new InetSocketAddress(host, port);
    bootstrap = new Bootstrap();
    eventLoopGroup = new NioEventLoopGroup(AVAILABLE_PROCESSOR_NUM * 2);
    bootstrap.group(eventLoopGroup)
        .channel(NioSocketChannel.class)
        .handler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addFirst(new ChannelInboundHandlerAdapter() {
              @Override
              public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                super.channelInactive(ctx);
                logger.error("Channel Broken, Begin To ReEstablish.");
                MethodSenderAndCallbackHandlerManager.remove(methodSenderAndCallbackHandler);
                ctx.channel().eventLoop().schedule(() -> doConnect(), 1, TimeUnit.SECONDS);
              }
            });
            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
            pipeline.addLast(new LengthFieldPrepender(4));
            pipeline.addLast(new KryoEncoder());
            pipeline.addLast(new KryoDecoder());
            pipeline.addLast(new MethodSenderAndCallbackHandler());
          }
        }).option(ChannelOption.SO_KEEPALIVE, true);

    doConnect();
  }

  public void close() {
    MethodSenderAndCallbackHandlerManager.remove(methodSenderAndCallbackHandler);
    LBNettyClientManager.remove(host + ":" + port);
    eventLoopGroup.shutdownGracefully();
    methodSenderAndCallbackHandler = null;
  }


  public void doConnect() {
    ChannelFuture channelFuture = bootstrap.connect(serverAddress);
    channelFuture.addListener(new ChannelFutureListener() {
      public void operationComplete(final ChannelFuture channelFuture) throws Exception {
        if (channelFuture.isSuccess()) {
          MethodSenderAndCallbackHandler handler = channelFuture.channel().pipeline().get(MethodSenderAndCallbackHandler.class);
          methodSenderAndCallbackHandler = handler;
          MethodSenderAndCallbackHandlerManager.put(methodSenderAndCallbackHandler);
          retryCount = 0;
          retryInterval = 3;
          logger.info("Connnect To Server Success, host: {}", host+":"+port);
        } else {
          if (retryCount >= 5) {
            logger.error("Can Not Connect To Server, host: {}", host+":"+port);
            close();
          } else {
            channelFuture.channel().eventLoop().schedule(new Runnable() {
              @Override
              public void run() {
                retryCount++;
                logger.warn("Retry Connect To Server, host: {}, Retry Times: {}", host+":"+port, retryCount);
                doConnect();
              }
            }, retryInterval, TimeUnit.SECONDS);
          }
          retryInterval = retryInterval + 3;
        }
      }
    });
  }

}