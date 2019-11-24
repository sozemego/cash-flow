import express = require("express");
import { Request, Response, NextFunction } from "express";
const jwt = require("jsonwebtoken");
import { service } from "./service";
const logger = require("../logger").namedLogger("router");

export const router = express.Router();

router.use((req: Request, res: Response, next: NextFunction) => {
  logger.info(
    "Request at " +
      new Date().toISOString() +
      " to " +
      req.path +
      " " +
      JSON.stringify(req.query)
  );
  const token = getJWT(req);
  if (!token) {
    return res.send(401).end();
  }
  next();
});

router.get("/player", async (req: Request, res: Response) => {
  const { id } = req.query;
  try {
    const player = await service.getPlayer(id);
    logger.info(`Returning ${JSON.stringify(player)}`);
    res.send(player);
  } catch (e) {
    res.status(400).send({ message: "Problem retrieving player!" });
  }
});

router.get("/players", async (req: Request, res: Response) => {
  try {
    const players = await service.getPlayers();
    logger.info(`Returning ${players.length} players`);
    res.send(players);
  } catch (e) {
    res.status(400).send({ message: "Problem retrieving players!" });
  }
});

router.post("/player", async (req: Request, res: Response) => {
  const { name } = req.query;
  const token = getJWT(req);
  try {
    const player = await service.createPlayer(name, token.id);
    logger.info(`Returning ${JSON.stringify(player)}`);
    res.send(player);
  } catch (e) {
    console.error(e);
    res.status(400).send({ message: "Problem creating player!" });
  }
});

router.get("/playerByUser", async (req: Request, res: Response) => {
  const { id } = req.query;
  const token = getJWT(req);
  const idFromToken = token.id;
  if (idFromToken !== id) {
    res
      .status(401)
      .send({ message: `Invalid token!` })
      .end();
    return;
  }

  try {
    const player = await service.getPlayerByUserId(id);
    logger.info(`Returning ${JSON.stringify(player)}`);
    res.send(player);
  } catch (e) {
    res.status(400).send({ message: "Problem retrieving player!" });
  }
});

router.post("/transfer", async (req: Request, res: Response) => {
  const { query } = req;
  const amount = Number(query.amount);
  const { id } = query;
  logger.info(`Call to transfer with ${amount}`);
  if (!amount) {
    res.status(400).end();
    return;
  }
  try {
    const transferResult = await service.transfer(amount, id);
    res.json(transferResult);
  } catch (e) {
    res.status(400).send(`Error when transfering cash to player id = ${id}`);
  }
});

function getJWT(request: Request): any | null {
  const { headers } = request;
  const authorization = headers.authorization;
  if (!authorization) {
    return null;
  }
  const token = authorization.split(" ")[1];
  if (!token) {
    return null;
  }
  return jwt.decode(token);
}
