package com.xxx.rpclite.service;

import com.xxx.rpclite.server.RpcServiceExporter;
import com.xxx.rpclite.service.impl.UserServiceImpl;
import com.xxx.rpclite.service.impl.WalletServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 * Created by klose on 2017/1/27.
 */
public class Server2 {

  public static void main(String[] args) {
    List impls = Arrays.asList(new UserServiceImpl(), new WalletServiceImpl());
    RpcServiceExporter exporter2 = new RpcServiceExporter(9999, 10, impls);
    System.out.println("server2 await.");
  }
}
