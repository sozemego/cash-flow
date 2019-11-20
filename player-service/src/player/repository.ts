import { Player } from "./service";

const { Pool } = require("pg");
const pool = new Pool();
const logger = require("../logger").namedLogger("player-repository");

pool.on("error", (err, client) => {
  console.error("Unexpected error on idle client", err);
  process.exit(-1);
});

export async function getPlayer(id: string): Promise<Player> {
  logger.info(`Fetching player id = ${id}`);
  if (!id) {
    throw new Error("Cannot fetch player without id");
  }
  const data = await pool.query("SELECT * FROM player.player WHERE id = $1", [
    id
  ]);
  return data.rows[0];
}

export async function getPlayerByUserId(userId: string): Promise<Player> {
  logger.info(`Fetching player userId = ${userId}`);
  if (!userId) {
    throw new Error("Cannot fetch player without user_id");
  }
  const data = await pool.query("SELECT * FROM player.player WHERE user_id = $1", [
    userId
  ]);
  return data.rows[0];
}

export async function updatePlayer(player): Promise<void> {
  logger.info(`Updating player ${JSON.stringify(player)}`);
  await pool.query(
    `UPDATE player.player SET cash = ${player.cash} WHERE id = '${player.id}'`
  );
}

export async function createPlayer(player: Player): Promise<Player> {
  logger.info(`Creating new player = ${JSON.stringify(player)}`);
  const result = await pool.query(
    `INSERT INTO player.player (USER_ID, NAME, CASH) VALUES ($1, $2, $3) RETURNING *`,
    [player.user_id, player.name, player.cash]
  );
  const createdPlayer = result.rows[0];
  logger.info(`Player = ${JSON.stringify(player)} created`);
  return createdPlayer;
}
