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
  await pool.query("SET SCHEMA 'game_event'");
  const data = await pool.query("SELECT * FROM game_event");
  const events = data.rows;
  logger.info(`Fetched ${events.length} events`);
  return data.rows.map((row: any) => {
    return {
      id: row.id,
      text: row.text,
      timestamp: Number(row.timestamp),
      type: row.type,
      level: row.level
    };
  });
}

const addEventQuery =
  "INSERT INTO game_event(id, text, timestamp, type, level) VALUES ($1, $2, $3, $4, $5)";

export async function addEvent(event: GameEvent): Promise<void> {
  logger.info(`Saving event = ${event.id}`);
  await pool.query("SET SCHEMA 'game_event'");
  const values = [
    event.id,
    event.text,
    event.timestamp,
    event.type,
    event.level
  ];
  return await pool.query(addEventQuery, values);
}
