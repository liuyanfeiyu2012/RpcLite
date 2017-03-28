package com.xxx.rpclite.transport.kryo;


import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.pool.KryoPool;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by klose on 2017/2/20.
 */
public class KryoEncoder extends MessageToByteEncoder<Object> {
  private static KryoPool pool = KryoPoolFactory.getKryoPool();

  protected void encode(final ChannelHandlerContext ctx, final Object msg, final ByteBuf out) throws Exception {
    encode(out, msg);
  }

  public void encode(final ByteBuf out, final Object message) throws IOException {
    Kryo kryo = pool.borrow();
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         Output output = new Output(byteArrayOutputStream)) {
      kryo.writeClassAndObject(output, message);
      byte[] body = byteArrayOutputStream.toByteArray();
      int dataLength = body.length;
      out.writeInt(dataLength);
      out.writeBytes(body);
    }finally {
      pool.release(kryo);
    }
  }
}

