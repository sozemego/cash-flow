import amqplib = require("amqplib");
import { handleEvent } from "./eventService";

const queueName = "game-event-queue";

export async function connectToQueue() {
  const connection = await amqplib.connect("amqp://localhost");
  const channel = await connection.createChannel();
  try {
    const ok = await channel.assertQueue(queueName, {
      arguments: { "x-queue-type": "classic" }
    });

    const consume = await channel.consume(queueName, message => {
      if (message != null) {
        handleEvent(message.content.toString());
        channel.ack(message);
      }
    });
    console.log(consume);
  } catch (e) {
    console.log(e);
  }
}
