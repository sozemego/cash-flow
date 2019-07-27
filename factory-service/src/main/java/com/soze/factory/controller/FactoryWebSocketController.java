package com.soze.factory.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class FactoryWebSocketController {

  @MessageMapping("/init")
  @SendTo("/topic/init-response")
  public String handleInit() {
    return "YES IM HERE";
  }

}
