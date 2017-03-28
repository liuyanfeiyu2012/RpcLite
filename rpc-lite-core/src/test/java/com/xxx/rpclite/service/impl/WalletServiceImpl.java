package com.xxx.rpclite.service.impl;

import com.xxx.rpclite.service.WalletService;

import java.math.BigDecimal;

/**
 * Created by klose on 2017/1/27.
 */
public class WalletServiceImpl implements WalletService{
  @Override
  public BigDecimal getBalance(Long userId) {
    return BigDecimal.valueOf(userId);
  }
}
