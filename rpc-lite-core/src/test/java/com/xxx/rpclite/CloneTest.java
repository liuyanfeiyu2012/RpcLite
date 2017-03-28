package com.xxx.rpclite;

import com.xxx.rpclite.model.MethodRequest;

/**
 * Created by klose on 2017/1/30.
 */
public class CloneTest {

  public static void main(String[] args) throws CloneNotSupportedException {
    MethodRequest methodRequest1 = new MethodRequest();
    methodRequest1.setId("1111111");
    methodRequest1.setServiceName("hhhh");
//    methodRequest1.setParameterTypes(new Class[]{Integer.class, Integer.class});
    methodRequest1.setParameterValues(new Object[]{1, 1});

    MethodRequest request = (MethodRequest) methodRequest1.clone();

    request.setId("2222222");
//    request.setParameterTypes(new Class[]{String.class, String.class});
    request.setParameterValues(new Object[]{"1111", "111"});

  }
}
