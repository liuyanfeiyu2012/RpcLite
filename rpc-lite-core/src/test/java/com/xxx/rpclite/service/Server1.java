package com.xxx.rpclite.service;

import com.xxx.rpclite.server.RpcServiceExporter;
import com.xxx.rpclite.service.impl.UserServiceImpl;
import com.xxx.rpclite.service.impl.WalletServiceImpl;

import java.util.Arrays;
import java.util.List;

/**
 * Created by klose on 2017/1/27.
 */
public class Server1 {

  public static void main(String[] args) {
    List impls = Arrays.asList(new UserServiceImpl(), new WalletServiceImpl());
    RpcServiceExporter exporter1 = new RpcServiceExporter(8888, 10, impls);
    System.out.println("server1 await.");
  }
}
