package com.xxx.serializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomCategoryDto implements Serializable {

  private static final long serialVersionUID = -2313124117202193679L;

  private String categoryCode;

  private String categoryName;

  private List<CustomItemDto> customItemList = new ArrayList<CustomItemDto>();

  private Set<CustomItemDto> customItemSet = new HashSet<CustomItemDto>();

  private Map<String, CustomItemDto> customItemMap = new HashMap<String, CustomItemDto>();

  public String getCategoryCode() {
    return categoryCode;
  }

  public void setCategoryCode(String categoryCode) {
    this.categoryCode = categoryCode;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  public List<CustomItemDto> getCustomItemList() {
    return customItemList;
  }

  public void setCustomItemList(List<CustomItemDto> customItemList) {
    this.customItemList = customItemList;
  }

  public Set<CustomItemDto> getCustomItemSet() {
    return customItemSet;
  }

  public void setCustomItemSet(Set<CustomItemDto> customItemSet) {
    this.customItemSet = customItemSet;
  }

  public Map<String, CustomItemDto> getCustomItemMap() {
    return customItemMap;
  }

  public void setCustomItemMap(Map<String, CustomItemDto> customItemMap) {
    this.customItemMap = customItemMap;
  }

}