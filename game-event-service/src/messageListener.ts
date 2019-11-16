import { Message } from "kafka-node";

const kafka = require("kafka-node");
import { handleAppEvent } from "./eventService";

const topicName = "game-event";

let consumer = null;

export async function connectToTopic() {
  const client = new kafka.KafkaClient({ kafkaHost: "localhost:9092" });
  consumer = new kafka.Consumer(client, [{ topic: topicName, partition: 0 }], {
    autoCommit: false
  });

  consumer.on("message", (message: Message) => {
    try {
      const event = JSON.parse(message.value as string);
      handleAppEvent(event);
    } catch (e) {

    }
  });
}
