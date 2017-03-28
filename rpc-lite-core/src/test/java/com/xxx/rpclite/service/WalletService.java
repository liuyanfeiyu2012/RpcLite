package com.xxx.rpclite.service;

import com.xxx.rpclite.annotation.RpcLite;

import java.math.BigDecimal;

/**
 * Created by klose on 2017/1/27.
 */
@RpcLite(serviceName = "walletServiceImpl", timeout = 5)
public interface WalletService {


  BigDecimal getBalance(Long userId);
}
