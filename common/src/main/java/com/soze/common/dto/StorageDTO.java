package com.soze.common.dto;

import java.util.HashMap;
import java.util.Map;

public class StorageDTO {

  private int capacity;
  private final Map<Resource, Integer> resources = new HashMap<>();

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public Map<Resource, Integer> getResources() {
    return resources;
  }
}
