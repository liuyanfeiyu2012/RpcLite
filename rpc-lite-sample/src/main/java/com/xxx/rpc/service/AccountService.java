package com.xxx.rpc.service;

import com.xxx.rpclite.annotation.RpcLite;

import java.math.BigDecimal;

/**
 * Created by klose on 2017/1/27.
 */
@RpcLite(serviceName = "AccountServiceImpl", timeout = 5)
public interface AccountService {

  BigDecimal getUserBalance(Long userId);
}
