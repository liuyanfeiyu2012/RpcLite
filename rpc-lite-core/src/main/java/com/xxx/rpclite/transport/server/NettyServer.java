package com.xxx.rpclite.transport.server;

import com.xxx.rpclite.server.MethodReceiveAndInvokerHandler;
import com.xxx.rpclite.transport.kryo.KryoDecoder;
import com.xxx.rpclite.transport.kryo.KryoEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by klose on 2017/1/26.
 */
public class NettyServer {
  private static Logger logger = LoggerFactory.getLogger(NettyServer.class);

  private static int DEFAULT_WORK_THREADS = 10;

  private static int AVAILABLE_PROCESSOR_NUM = Runtime.getRuntime().availableProcessors();

  private static int RETRY_TIME = 0;

  private int port;

  private int workThreads = DEFAULT_WORK_THREADS;

  private Map<String, Object> serviceImpls;

  private volatile EventLoopGroup boss;
  private volatile EventLoopGroup worker;
  private volatile ServerBootstrap bootstrap;

  private Lock lock = new ReentrantLock();
  private Condition complete = lock.newCondition();

  protected void start(int port, int workThreads, Map<String, Object> serviceImpls) throws InterruptedException {
    this.port = port;
    this.workThreads = workThreads;
    this.serviceImpls = serviceImpls;
    init();
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
          //never happen.
        }
        doConnect();
      }
    }).start();
    try {
      lock.lock();
      complete.await();
    } finally {
      lock.unlock();
    }
  }

  private void init() throws InterruptedException {
    bootstrap = new ServerBootstrap();
    boss = new NioEventLoopGroup();
    worker = new NioEventLoopGroup(AVAILABLE_PROCESSOR_NUM * 2);
    bootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childHandler(new ChannelInitializer<SocketChannel>() {
          @Override
          protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
            pipeline.addLast(new LengthFieldPrepender(4));
            pipeline.addLast(new KryoEncoder());
            pipeline.addLast(new KryoDecoder());
            pipeline.addLast(new MethodReceiveAndInvokerHandler(workThreads, serviceImpls));
          }
        })
        .option(ChannelOption.SO_BACKLOG, 128)
        .childOption(ChannelOption.SO_KEEPALIVE, true);
  }

  public void doConnect() {
    ChannelFuture future = bootstrap.bind(port);
    future.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture future) throws Exception {
        if (future.isSuccess()) {
          logger.info("Netty Server Start, Listen On Port: {}", port);
          try {
            lock.lock();
            complete.signal();
          } finally {
            lock.unlock();
          }
        } else {
          if (RETRY_TIME < 3) {
            future.channel().eventLoop().schedule(() -> doConnect(), 2, TimeUnit.SECONDS);
            logger.warn("Netty Server Failed, And Begin To Retry :{} Time.", RETRY_TIME);
            RETRY_TIME++;
          } else {
            logger.error("Netty Server Failed, Port: {}", port);
            future.channel().eventLoop().schedule(() -> close(), 1, TimeUnit.SECONDS);
          }
        }
      }
    });
  }

  public void close() {
    boss.shutdownGracefully();
    worker.shutdownGracefully();
    try {
      lock.lock();
      complete.signal();
    } finally {
      lock.unlock();
    }
    boss = null;
    worker = null;
    bootstrap = null;
    logger.info("Netty Server Close Success.");
  }
}
