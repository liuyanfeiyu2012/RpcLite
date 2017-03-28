package com.xxx.rpclite.service;

import com.xxx.rpclite.entity.POJO;

import java.util.Date;

/**
 * Created by klose on 2017/1/27.
 */
public class Client {

  public static void main(String[] args) throws InterruptedException {
//    UserService userService = new RpcServiceProxyBean.Builder().remoteAddress("localhost:8888").build(UserService.class);
//    WalletService walletService = new RpcServiceProxyBean.Builder().remoteAddress
//        ("localhost:8888").build(WalletService.class);
//
//    BigDecimal hhh = walletService.getBalance(1234l);
//    System.out.println(hhh);
//
//    System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhh");

    int count = 0;
    System.out.println(new Byte("77"));
//    while (true){
//      try{
//        doTest(userService);
//      }catch (Exception e){
//        System.out.println("during error time." + e.getMessage());
//      }
//      System.out.println("count : "+count);
//      if (count > 20){
//        break;
//      }
      count ++;


//      Thread.sleep(3000);
//    }

  }

  public static void doTest(UserService userService){
    POJO pojo = userService.testPojo1(new Byte("77"), new Byte[]{new Byte("78"), new Byte("79")},
        new Date(),
        new Date[]{new Date(), new Date(new Date().getTime() - 100000)});

//    for (Date date1 : pojo.getDates()) {
//      System.out.println("dates : " + date1);
//    }
    System.out.println("pojo received : ");
    System.out.println("date : " + pojo.getDate());
//    System.out.println("byte: " + pojo.getValue());
//    System.out.println("bytes : "+pojo.getValues().length);
    System.out.println("----------------------------");
  }
}
