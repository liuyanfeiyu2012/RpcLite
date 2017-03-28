package com.xxx.serializable;

import java.io.Serializable;

/**
 * Created by klose on 2017/2/14.
 */
public class CustomItemDto implements Serializable{
  private static final long serialVersionUID = -3003816932176037869L;

  private Long id;

  private String itemCode;

  private String itemName;

  private String itemMemo;

  private double itemPrice;

  private double itemDespositPrice;

  private Integer sort;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getItemCode() {
    return itemCode;
  }

  public void setItemCode(String itemCode) {
    this.itemCode = itemCode;
  }

  public String getItemName() {
    return itemName;
  }

  public void setItemName(String itemName) {
    this.itemName = itemName;
  }

  public String getItemMemo() {
    return itemMemo;
  }

  public void setItemMemo(String itemMemo) {
    this.itemMemo = itemMemo;
  }

  public double getItemPrice() {
    return itemPrice;
  }

  public void setItemPrice(double itemPrice) {
    this.itemPrice = itemPrice;
  }

  public double getItemDespositPrice() {
    return itemDespositPrice;
  }

  public void setItemDespositPrice(double itemDespositPrice) {
    this.itemDespositPrice = itemDespositPrice;
  }

  public Integer getSort() {
    return sort;
  }

  public void setSort(Integer sort) {
    this.sort = sort;
  }
}
