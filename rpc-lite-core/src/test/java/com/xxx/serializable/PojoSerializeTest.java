package com.xxx.serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.xxx.rpclite.entity.POJO;

import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by klose on 2017/2/15.
 *
 */
public class PojoSerializeTest {

  public static void main(String[] args) {
    POJO pojo = new POJO();
    pojo.setDate(new Date());
    pojo.setDates(Arrays.asList(new Date(), new Date()));
//    pojo.setValue("a".getBytes()[0]);
//    pojo.setValues("a".getBytes());

    Object obj = pojo;

    byte[] bs = serializationObject(obj);
    POJO newPojo = deserializationObject(bs, POJO.class);

//    Assert.assertArrayEquals(pojo.getValues(), newPojo.getValues());
    Assert.assertArrayEquals(pojo.getDates().toArray(new Date[]{}), newPojo.getDates().toArray
        (new Date[]{}));
  }


  public static <T> byte[] serializationObject(T obj) {
    Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.register(obj.getClass(), new JavaSerializer());

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Output output = new Output(baos);
    kryo.writeClassAndObject(output, obj);
    output.flush();
    output.close();

    byte[] b = baos.toByteArray();
    try {
      baos.flush();
      baos.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return b;
  }

  public static <T> T deserializationObject(byte[] b, Class<T> clazz) {
    Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.register(clazz, new JavaSerializer());

    ByteArrayInputStream bais = new ByteArrayInputStream(b);
    Input input = new Input(bais);
    return (T) kryo.readClassAndObject(input);
  }
}
