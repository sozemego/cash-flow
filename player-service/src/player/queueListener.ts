import amqplib = require("amqplib");
import { handleUserCreated } from "./service";

const logger = require("../logger").namedLogger("queue-listener");

const exchangeName = "domain-event-queue";

let connection = null;
let queueName = null;

export async function connectToExchange() {
  connection = await amqplib.connect("amqp://localhost");
  const channel = await connection.createChannel();
  try {
    const queue = await channel.assertQueue("", { exclusive: true });
    queueName = queue.queue;
    await channel.bindQueue(queueName, exchangeName, "");

    const consume = await channel.consume(queueName, async message => {
      if (message != null) {
        const event = JSON.parse(message.content.toString());
        if (event.type === USER_CREATED) {
          await handleUserCreated(event);
        }
        channel.ack(message);
      }
    });
    logger.info(JSON.stringify(consume));
  } catch (e) {
    console.log(e);
  }
}

export const USER_CREATED = "USER_CREATED";

export interface UserCreated {
  id: string;
  name: string;
  createTime: string;
  type: typeof USER_CREATED;
}
