package com.soze.common.ws.factory.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Objects;
import java.util.UUID;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true
)
@JsonSubTypes(value = {
    @JsonSubTypes.Type(value = ResourceProduced.class, name = "RESOURCE_PRODUCED")
})
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ServerMessage {

  private final UUID messageId;
  private final String type;

  public ServerMessage(UUID messageId, String type) {
    Objects.requireNonNull(this.messageId = messageId);
    Objects.requireNonNull(this.type = type);
  }

  public UUID getMessageId() {
    return messageId;
  }

  public String getType() {
    return type;
  }

  public enum ServerMessageType {
    RESOURCE_PRODUCED
  }
}
