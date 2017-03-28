package com.xxx.rpclite.transport.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.pool.KryoPool;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by klose on 2017/2/20.
 */
public class KryoDecoder extends ByteToMessageDecoder {
  private static Logger logger = LoggerFactory.getLogger(KryoDecoder.class);

  private static final int MESSAGE_LENGTH = 4;

  private static KryoPool pool = KryoPoolFactory.getKryoPool();

  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
    if (in.readableBytes() < MESSAGE_LENGTH) {
      return;
    }
    in.markReaderIndex();
    int messageLength = in.readInt();

    if (messageLength < 0) {
      ctx.close();
    }
    if (in.readableBytes() < messageLength) {
      in.resetReaderIndex();
      return;
    } else {
      byte[] messageBody = new byte[messageLength];
      in.readBytes(messageBody);
      Object obj = decode(messageBody);
      out.add(obj);
    }
  }

  public Object decode(byte[] body) {
    Object result = null;
    Kryo kryo = pool.borrow();
    try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
         Input in = new Input(byteArrayInputStream)) {
      result = kryo.readClassAndObject(in);
    } catch (IOException e) {
      logger.error("Kryo Decode Failed, e: {}", e);
    }finally {
      pool.release(kryo);
    }
    return result;
  }
}
