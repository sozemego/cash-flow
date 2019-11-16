const kafka = require("kafka-node");
import { Message } from "kafka-node";

import { handleUserCreated } from "./service";
import { USER_CREATED } from "./queueListener";

const topicName = "domain";

let consumer = null;

export async function connectToTopic() {
  const client = new kafka.KafkaClient({ kafkaHost: "localhost:9092" });
  consumer = new kafka.Consumer(client, [{ topic: topicName, partition: 0 }], {
    autoCommit: false
  });

  consumer.on("message", async (message: Message) => {
    try {
      const event = JSON.parse(message.value as string);
      if (event.type === USER_CREATED) {
        await handleUserCreated(event);
      }
    } catch (e) {
      console.error(e);
    }
  });
}
