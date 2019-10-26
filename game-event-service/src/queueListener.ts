import amqplib = require("amqplib");
import { handleAppEvent } from "./eventService";

const queueName = "game-event-queue";

export async function connectToQueue() {
  const connection = await amqplib.connect("amqp://localhost");
  const channel = await connection.createChannel();
  try {
    const ok = await channel.assertQueue(queueName, {
      arguments: { "x-queue-type": "classic" }
    });

    const consume = await channel.consume(queueName, async message => {
      if (message != null) {
        const json = JSON.parse(message.content.toString());
        await handleAppEvent(json);
        channel.ack(message);
      }
    });
    console.log(consume);
  } catch (e) {
    console.log(e);
  }
}
