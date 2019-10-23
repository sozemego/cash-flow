import { Player } from "./service";

const { Pool } = require("pg");
const pool = new Pool();
const logger = require("../logger").namedLogger("player-repository");

pool.on("error", (err, client) => {
  console.error("Unexpected error on idle client", err);
  process.exit(-1);
});

export async function getPlayer(): Promise<Player> {
  logger.info("Fetching player");
  await pool.query("SET SCHEMA 'player'");
  const data = await pool.query("SELECT * FROM player");
  return data.rows[0];
}

export async function updatePlayer(player): Promise<void> {
  logger.info(`Updating player ${JSON.stringify(player)}`);
  await pool.query("SET SCHEMA 'player'");
  await pool.query(`UPDATE player SET cash = ${player.cash} WHERE id = '${player.id}'`);
}
