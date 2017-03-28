package com.xxx.serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.JavaSerializer;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

/**
 * Created by klose on 2017/2/17.
 */
public class ExtendTest {

  public static class Animal implements Serializable {
    public String name;

    public Integer category;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public Integer getCategory() {
      return category;
    }

    public void setCategory(Integer category) {
      this.category = category;
    }
  }

  @Test
  public void test() {

    Cat cat = new Cat();
    cat.setSet(28);
    cat.setCategory(10);
    cat.setName("cat");

    byte[] bytes = serializationObject(cat);
    Cat newCate = deserializationObject(bytes, Cat.class);
    Object object = deserializationObject(bytes, Animal.class);

    Assert.assertEquals(cat.getName(), newCate.getName());
    Assert.assertEquals(cat.getCategory(), newCate.getCategory());
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
