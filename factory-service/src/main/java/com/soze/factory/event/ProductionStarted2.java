package com.soze.factory.event;

import java.time.LocalDateTime;

public class ProductionStarted2 extends Event {

  public long productionStartTime;

  public ProductionStarted2() {
  }

  public ProductionStarted2(String entityId, LocalDateTime timestamp, int version, long productionStartTime) {
    super(entityId, timestamp, version);
    this.productionStartTime = productionStartTime;
  }

  @Override
  public EventType getType() {
    return EventType.PRODUCTION_STARTED2;
  }

  @Override
  public void accept(EventVisitor visitor) {
    visitor.visit(this);
  }

  @Override
  public String toString() {
    return "ProductionStarted2{" +
        "productionStartTime=" + productionStartTime +
        ", entityId='" + entityId + '\'' +
        ", timestamp=" + timestamp +
        ", version=" + version +
        '}';
  }
}
