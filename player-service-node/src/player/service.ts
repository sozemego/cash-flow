import * as fs from "fs";
import { registry } from "./socketRegistry";
const logger = require("../logger").namedLogger("player-service");

interface PlayerService {
  transfer: (number) => TransferResult;
  getPlayer: () => Player;
}

interface TransferResult {
  amountTransferred: number;
  current: number;
}

interface Player {
  name: string;
  cash: number;
}

const playerString = fs.readFileSync("player.json", { encoding: "utf-8" });
const player: Player = JSON.parse(playerString);

function transfer(amount): TransferResult {
  logger.info(
    `Attempting to transfer ${amount} to player ${JSON.stringify(player)}`
  );

  const { cash } = player;

  if (cash < Math.abs(amount)) {
    return { amountTransferred: 0, current: player.cash };
  }

  player.cash += amount;
  savePlayer();
  syncCashChange(amount);
  return { amountTransferred: amount, current: player.cash };
}

function savePlayer() {
  logger.info("Saving player");
  fs.writeFileSync("player.json", JSON.stringify(player));
}

function syncCashChange(amount) {
  const sockets = registry.getSockets();
  logger.info(`Sync player to ${sockets.length} sockets`);
  sockets.forEach(socket =>
    socket.send(JSON.stringify({ type: "PLAYER_CASH_CHANGED", amount }))
  );
}

export const service: PlayerService = {
  getPlayer: () => player,
  transfer
};
