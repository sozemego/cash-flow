import { registry } from "./socketRegistry";
import {
  createPlayer as repoCreatePlayer,
  getPlayer,
  getPlayers,
  getPlayerByUserId,
  updatePlayer
} from "./repository";
import { sendDomainMessage } from "./messageListener";
const logger = require("../logger").namedLogger("player-service");

interface PlayerService {
  transfer: (number: number, id: string) => Promise<TransferResult>;
  getPlayer: (id: string) => Promise<Player>;
  getPlayers: () => Promise<Player[]>;
  getPlayerByUserId: (id: string) => Promise<Player>;
  createPlayer: (name: string, userId: string) => Promise<Player>;
}

interface TransferResult {
  amountTransferred: number;
  current: number;
}

export interface Player {
  id: string | null;
  name: string;
  cash: number;
  user_id: string;
}

export async function handleUserCreated(userCreated: UserCreated) {
  logger.info(`Handling UserCreated for user ${userCreated.name}`);

  const existingPlayer = await getPlayerByUserId(userCreated.id);
  if (existingPlayer) {
    return;
  }

  try {
    const player = await createPlayer(userCreated.name, userCreated.id);
    await sendDomainMessage(createPlayerCreatedMessage(player));
  } catch (e) {
    logger.warn(
      `Problem creating player for userId = ${userCreated.id} = ${e}`
    );
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

function createPlayerCreatedMessage(player: Player): PlayerCreated {
  return {
    playerId: player.id,
    playerName: player.name,
    userId: player.user_id,
    type: PLAYER_CREATED
  };
}

async function createPlayer(name: string, userId: string) {
  const player: Player = {
    id: null,
    name,
    user_id: userId,
    cash: 5000
  };
  logger.info(`Creating player = ${JSON.stringify(player)}`);
  const createdPlayer = await repoCreatePlayer(player);
  await sendDomainMessage(createPlayerCreatedMessage(createdPlayer));
  return createdPlayer;
}

export const service: PlayerService = {
  getPlayer,
  getPlayers,
  transfer,
  getPlayerByUserId,
  createPlayer
};

export const USER_CREATED = "USER_CREATED";

export interface UserCreated {
  id: string;
  name: string;
  createTime: string;
  type: typeof USER_CREATED;
}

export const PLAYER_CREATED = "PLAYER_CREATED";

export interface PlayerCreated {
  userId: string;
  playerId: string;
  playerName: string;
  type: typeof PLAYER_CREATED;
}
