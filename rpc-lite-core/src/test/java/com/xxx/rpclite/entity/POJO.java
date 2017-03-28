package com.xxx.rpclite.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by klose on 2017/1/29.
 */
public class POJO implements Serializable{

  private static final long serialVersionUID = 1;

//  private String nullValue;

  private Byte value;

  private byte[] values;

  private Date date;

  private List<Date> dates;

  public POJO(){}

  public void setValue(Byte value) {
    this.value = value;
  }

//  public String getNullValue() {
//    return nullValue;
//  }
//
//  public void setNullValue(String nullValue) {
//    this.nullValue = nullValue;
//  }

  public byte getValue() {
    return value;
  }

  public void setValue(byte value) {
    this.value = value;
  }

  public byte[] getValues() {
    return values;
  }

  public void setValues(byte[] values) {
    this.values = values;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public List<Date> getDates() {
    return dates;
  }

  public void setDates(List<Date> dates) {
    this.dates = dates;
  }
}
