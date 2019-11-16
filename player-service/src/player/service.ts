import { registry } from "./socketRegistry";
import {
  createPlayer,
  getPlayer,
  getPlayerByUserId,
  updatePlayer
} from "./repository";
import { UserCreated } from "./queueListener";
const logger = require("../logger").namedLogger("player-service");

interface PlayerService {
  transfer: (number: number, id: string) => Promise<TransferResult>;
  getPlayer: (id: string) => Promise<Player>;
  getPlayerByUserId: (id: string) => Promise<Player>;
}

interface TransferResult {
  amountTransferred: number;
  current: number;
}

export interface Player {
  name: string;
  cash: number;
  user_id: string;
}

export async function handleUserCreated(userCreated: UserCreated) {
  logger.info(`Handling UserCreated for user ${userCreated.name}`);
  const player: Player = {
    name: userCreated.name,
    cash: 5000,
    user_id: userCreated.id
  };

  const existingPlayer = await getPlayerByUserId(userCreated.id);
  if (existingPlayer) {
    return;
  }

  try {
    await createPlayer(player);
  } catch (e) {
    logger.warn(`Problem creating player ${JSON.stringify(player)} = ${e}`);
  }
}

async function transfer(amount: number, id: string): Promise<TransferResult> {
  const player = await getPlayer(id);
  logger.info(
    `Attempting to transfer ${amount} to player ${JSON.stringify(player)}`
  );

  const { cash } = player;

  if (cash < Math.abs(amount)) {
    return { amountTransferred: 0, current: player.cash };
  }

  player.cash += amount;
  await updatePlayer(player);
  syncCashChange(amount);
  return { amountTransferred: amount, current: player.cash };
}

function syncCashChange(amount) {
  const sockets = registry.getSockets();
  logger.info(`Sync player to ${sockets.length} sockets`);
  sockets.forEach(socket =>
    socket.send(JSON.stringify({ type: "PLAYER_CASH_CHANGED", amount }))
  );
}

export const service: PlayerService = {
  getPlayer,
  transfer,
  getPlayerByUserId
};
