import { registry } from "./socketRegistry";
import { getPlayer, updatePlayer } from "./repository";
const logger = require("../logger").namedLogger("player-service");

interface PlayerService {
  transfer: (number) => Promise<TransferResult>;
  getPlayer: () => Promise<Player>;
}

interface TransferResult {
  amountTransferred: number;
  current: number;
}

export interface Player {
  name: string;
  cash: number;
}

async function transfer(amount): Promise<TransferResult> {
  const player = await getPlayer();
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
  transfer
};
