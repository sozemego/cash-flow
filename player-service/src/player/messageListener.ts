const kafka = require("kafka-node");
import { Message, Producer, ProduceRequest } from "kafka-node";

import { handleUserCreated, USER_CREATED } from "./service";
const logger = require("../logger").namedLogger("message-listener");

const topicName = "domain";

let consumer = null;
let producer = null;

export async function connectToTopic() {
  const client = new kafka.KafkaClient({ kafkaHost: "localhost:9092", autoConnect: true });
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

  const producerClient = new kafka.KafkaClient({ kafkaHost: "localhost:9092", autoConnect: true });
  producer = new Producer(producerClient);
}

export async function sendDomainMessage(message: object) {
  return sendMessage("domain", message);
}

export async function sendMessage(topic: string, message: object) {
  const produceRequest: ProduceRequest = {
    topic,
    messages: JSON.stringify(message),
  };

  if (!producer) {
    logger.info(`Cannot send message to ${topic}, producer is not created`);
    return;
  }

  producer.send([produceRequest], (error, result) => {
    // @ts-ignore
    logger.info(`Message of type = ${message.type} sent to topic = ${topic}`);
    console.log(error);
    console.log(result);
  });
}
