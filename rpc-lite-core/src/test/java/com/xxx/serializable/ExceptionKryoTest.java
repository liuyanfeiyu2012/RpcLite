package com.xxx.serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.xxx.rpclite.service.UserService;
import com.xxx.rpclite.service.UserServiceException;
import com.xxx.rpclite.service.impl.UserServiceImpl;
import com.xxx.serializable.exceptions.Adder;
import com.xxx.serializable.exceptions.Exception1;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Created by klose on 2017/2/17.
 */
public class ExceptionKryoTest {

  @Test
  public void test1() {
    Exception1 exception1 = new Exception1();
    exception1.setErrorCode(404);
    exception1.setMsg("Page Not Fount.");

    byte[] bytes = serializationObject(exception1);
    Exception1 newException1 = deserializationObject(bytes, Exception1.class);
    Assert.assertEquals(exception1.getMsg(), newException1.getMsg());
    Assert.assertEquals(exception1.getErrorCode(), newException1.getErrorCode());
  }

//  @Test
//  public void test2() {
//    try {
//      new Adder().sumWithException(1, 1);
//    } catch (Exception1 exception1) {
//
//      byte[] bytes = serializationObject(exception1);
//      Exception1 newException1 = deserializationObject(bytes, Exception1.class);
//      Assert.assertEquals(exception1.getMsg(), newException1.getMsg());
//      Assert.assertEquals(exception1.getErrorCode(), newException1.getErrorCode());
//    }
//  }

  @Test
  public void test3() {
//    Exception1 exception1 = new Exception1();
//    exception1.setErrorCode(404);
//    exception1.setMsg("Page Not Fount.");
//    byte[] bytes = serializationObject(exception1);
//    Exception throwable = deserializationObject(bytes, Exception.class);
//    try {
//      throw throwable;
//    } catch (Throwable throwable1) {
//      if (throwable1 instanceof Exception1) {
//        Exception1 exception11 = (Exception1) throwable1;
//        Assert.assertEquals(exception1.getMsg(), exception11.getMsg());
//        Assert.assertEquals(exception1.getErrorCode(), exception11.getErrorCode());
//      }
//    }

    try {
      int a = new Adder().sumWithException(1, 1);
    }
// catch (Exception1 exception1){
//      System.out.println("ssss");
//    }
    catch (Throwable exception1) {
      byte[] bytes = serializationObject(exception1);
      Exception exception = deserializationObject(bytes, Exception.class);
      try {
        throw exception;
      } catch (Exception1 e1) {
        e1.printStackTrace();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  public void test2() throws NoSuchMethodException {
    Class clazz = UserService.class;
    Method[] methods = clazz.getDeclaredMethods();
    Class<?>[] clazzs;
    for (Method method : methods) {
      System.out.println(method.getName());
      if (method.getName().equals("getUserName")) {
        clazzs = method.getExceptionTypes();
        try {
          new UserServiceImpl().getUserName(2);
        } catch (Exception e) {
          if (e.getClass().equals(clazzs[0]) ){
            System.out.println("hhhh");
          }
        }
      }
    }
  }


  private <T extends Serializable> byte[] serializationObject(T obj) {
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

  private <T extends Serializable> T deserializationObject(byte[] b, Class<T> clazz) {
    Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.register(clazz, new JavaSerializer());

    ByteArrayInputStream bais = new ByteArrayInputStream(b);
    Input input = new Input(bais);
    return (T) kryo.readClassAndObject(input);
  }
}
