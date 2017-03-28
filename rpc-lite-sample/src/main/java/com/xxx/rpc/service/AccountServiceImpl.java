package com.xxx.rpc.service;


import java.math.BigDecimal;

/**
 * Created by klose on 2017/1/27.
 */
public class AccountServiceImpl implements AccountService {
  @Override
  public BigDecimal getUserBalance(Long userId) {
    return BigDecimal.valueOf(userId);
  }
}
