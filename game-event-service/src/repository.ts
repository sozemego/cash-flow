import { Client } from "pg";
import { GameEvent } from "./types";

const { Pool } = require("pg");
const pool = new Pool();
const logger = require("./logger").namedLogger("game-event-repository");

pool.on("error", (err: Error, client: Client) => {
  console.error("Unexpected error on idle client", err);
  process.exit(-1);
});

export async function getEvents(): Promise<GameEvent[]> {
  logger.info("Fetching events");
  const data = await pool.query("SELECT * FROM game_event.game_event ORDER BY TIMESTAMP DESC LIMIT 30");
  const events = data.rows;
  logger.info(`Fetched ${events.length} events`);
  return data.rows.map((row: any) => {
    return {
      id: row.id,
      text: row.text,
      timestamp: Number(row.timestamp),
      type: "GAME_EVENT",
      level: row.level
    };
  });
}

const addEventQuery =
  "INSERT INTO game_event.game_event(id, text, timestamp, type, level) VALUES ($1, $2, $3, $4, $5)";

export async function addEvent(event: GameEvent): Promise<void> {
  logger.info(`Saving event = ${event.id}`);
  const values = [
    event.id,
    event.text,
    event.timestamp,
    event.type,
    event.level
  ];
  return await pool.query(addEventQuery, values);
}
