package com.xxx.serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.CollectionSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import com.esotericsoftware.kryo.serializers.MapSerializer;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class KryoTest {

  @Test
  public void testObject() {
    CustomItemDto val = new CustomItemDto();
    val.setId(Long.parseLong(String.valueOf(1)));
    val.setItemCode("");
    val.setItemDespositPrice(32.45);
    val.setItemMemo(null);
    val.setItemName("张金");
    val.setItemPrice(89.02);
    val.setSort(10);

    byte[] a = serializationObject(val);
    CustomItemDto newValue = deserializationObject(a, CustomItemDto.class);
    Assert.assertEquals(val.getId(), newValue.getId());
  }

  @Test
  public void testList() {
    List<CustomItemDto> lst = new ArrayList<CustomItemDto>();
    for (int i = 0; i < 10; i++) {
      CustomItemDto val = new CustomItemDto();
      val.setId(Long.parseLong(String.valueOf(i)));
      val.setItemCode("");
      val.setItemDespositPrice(32.45);
      val.setItemMemo(null);
      val.setItemName("张金");
      val.setItemPrice(89.02);
      val.setSort(10);
      lst.add(val);
    }

    byte[] a = serializationList(lst, CustomItemDto.class);
    List<CustomItemDto> newValue = deserializationList(a, CustomItemDto.class);
    Assert.assertEquals(lst.size(), newValue.size());
  }

  @Test()
  public void testBean() {
    CustomCategoryDto dto = new CustomCategoryDto();
    dto.setCategoryCode("ABCD_001");
    dto.setCategoryName("呼吸系统");
    for (int i = 0; i < 10; i++) {
      CustomItemDto val = new CustomItemDto();
      val.setId(Long.parseLong(String.valueOf(i)));
      val.setItemCode("");
      val.setItemDespositPrice(32.45);
      val.setItemMemo(null);
      val.setItemName("张金");
      val.setItemPrice(89.02);
      val.setSort(10);
      dto.getCustomItemList().add(val);
    }
    for (int i = 0; i < 10; i++) {
      CustomItemDto val = new CustomItemDto();
      val.setId(Long.parseLong(String.valueOf(i)));
      val.setItemCode("");
      val.setItemDespositPrice(32.45);
      val.setItemMemo(null);
      val.setItemName("张金");
      val.setItemPrice(89.02);
      val.setSort(10);
      dto.getCustomItemSet().add(val);
    }
    for (int i = 0; i < 10; i++) {
      CustomItemDto val = new CustomItemDto();
      val.setId(Long.parseLong(String.valueOf(i)));
      val.setItemCode("");
      val.setItemDespositPrice(32.45);
      val.setItemMemo(null);
      val.setItemName("张金");
      val.setItemPrice(89.02);
      val.setSort(10);
      dto.getCustomItemMap().put(String.valueOf(i), val);
    }

    byte[] a = serializationObject(dto);
    CustomCategoryDto newValue = deserializationObject(a, CustomCategoryDto.class);
    Assert.assertEquals(dto.getCategoryCode(), newValue.getCategoryCode());
  }

  @Test()
  public void testMap() {
    Map<String, CustomItemDto> map = new HashMap<>();
    for (int i = 0; i < 10; i++) {
      CustomItemDto val = new CustomItemDto();
      val.setId(Long.parseLong(String.valueOf(i)));
      val.setItemCode("");
      val.setItemDespositPrice(32.45);
      val.setItemMemo(null);
      val.setItemName("张金");
      val.setItemPrice(89.02);
      val.setSort(10);
      map.put("key" + i, val);
    }

    byte[] a = serializationMap(map, CustomItemDto.class);
    Map<String, CustomItemDto> newValue = deserializationMap(a, CustomItemDto.class);
    Assert.assertEquals(map.size(), newValue.size());
  }

  @Test()
  public void testSet() {
    Set<CustomItemDto> set = new HashSet<CustomItemDto>();
    for (int i = 0; i < 10; i++) {
      CustomItemDto val = new CustomItemDto();
      val.setId(Long.parseLong(String.valueOf(i)));
      val.setItemCode("");
      val.setItemDespositPrice(32.45);
      val.setItemMemo(null);
      val.setItemName("金星");
      val.setItemPrice(89.02);
      val.setSort(10);
      set.add(val);
    }

    byte[] a = serializationSet(set, CustomItemDto.class);
    Set<CustomItemDto> newValue = deserializationSet(a, CustomItemDto.class);
    Assert.assertEquals(set.size(), newValue.size());
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

  private <T extends Serializable> byte[] serializationList(List<T> obj, Class<T> clazz) {
    Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.setRegistrationRequired(true);

    CollectionSerializer serializer = new CollectionSerializer();
    serializer.setElementClass(clazz, new JavaSerializer());
    serializer.setElementsCanBeNull(false);

    kryo.register(clazz, new JavaSerializer());
    kryo.register(ArrayList.class, serializer);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Output output = new Output(baos);
    kryo.writeObject(output, obj);
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

  @SuppressWarnings("unchecked")
  private <T extends Serializable> List<T> deserializationList(byte[] obj, Class<T> clazz) {
    Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.setRegistrationRequired(true);

    CollectionSerializer serializer = new CollectionSerializer();
    serializer.setElementClass(clazz, new JavaSerializer());
    serializer.setElementsCanBeNull(false);

    kryo.register(clazz, new JavaSerializer());
    kryo.register(ArrayList.class, serializer);

    ByteArrayInputStream bais = new ByteArrayInputStream(obj);
    Input input = new Input(bais);
    return (List<T>) kryo.readObject(input, ArrayList.class, serializer);
  }

  private <T extends Serializable> byte[] serializationMap(Map<String, T> obj, Class<T> clazz) {
    Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.setRegistrationRequired(true);

    MapSerializer serializer = new MapSerializer();
    serializer.setKeyClass(String.class, new JavaSerializer());
    serializer.setKeysCanBeNull(false);
    serializer.setValueClass(clazz, new JavaSerializer());
    serializer.setValuesCanBeNull(true);

    kryo.register(clazz, new JavaSerializer());
    kryo.register(HashMap.class, serializer);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Output output = new Output(baos);
    kryo.writeObject(output, obj);
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

  @SuppressWarnings("unchecked")
  private <T extends Serializable> Map<String, T> deserializationMap(byte[] obj, Class<T> clazz) {
    Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.setRegistrationRequired(true);

    MapSerializer serializer = new MapSerializer();
    serializer.setKeyClass(String.class, new JavaSerializer());
    serializer.setKeysCanBeNull(false);
    serializer.setValueClass(clazz, new JavaSerializer());
    serializer.setValuesCanBeNull(true);

    kryo.register(clazz, new JavaSerializer());
    kryo.register(HashMap.class, serializer);

    ByteArrayInputStream bais = new ByteArrayInputStream(obj);
    Input input = new Input(bais);
    return (Map<String, T>) kryo.readObject(input, HashMap.class, serializer);
  }

  public static <T extends Serializable> byte[] serializationSet(Set<T> obj, Class<T> clazz) {
    Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.setRegistrationRequired(true);

    CollectionSerializer serializer = new CollectionSerializer();
    serializer.setElementClass(clazz, new JavaSerializer());
    serializer.setElementsCanBeNull(false);

    kryo.register(clazz, new JavaSerializer());
    kryo.register(HashSet.class, serializer);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    Output output = new Output(baos);
    kryo.writeObject(output, obj);
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

  @SuppressWarnings("unchecked")
  public static <T extends Serializable> Set<T> deserializationSet(byte[] obj, Class<T> clazz) {
    Kryo kryo = new Kryo();
    kryo.setReferences(false);
    kryo.setRegistrationRequired(true);

    CollectionSerializer serializer = new CollectionSerializer();
    serializer.setElementClass(clazz, new JavaSerializer());
    serializer.setElementsCanBeNull(false);

    kryo.register(clazz, new JavaSerializer());
    kryo.register(HashSet.class, serializer);

    ByteArrayInputStream bais = new ByteArrayInputStream(obj);
    Input input = new Input(bais);
    return (Set<T>) kryo.readObject(input, HashSet.class, serializer);
  }
}