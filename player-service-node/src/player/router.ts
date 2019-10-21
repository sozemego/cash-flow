import express = require("express");
import { Request, Response, NextFunction } from "express";
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
  next();
});

router.get("/player", async (req: Request, res: Response) => {
  const player = await service.getPlayer();
  logger.info(`Returning ${JSON.stringify(player)}`);
  res.send(player);
});

router.post("/transfer", async (req: Request, res: Response) => {
  const { query } = req;
  const amount = Number(query.amount);
  logger.info(`Call to transfer with ${amount}`);
  if (!amount) {
    res.status(400).end();
    return;
  }

  const transferResult = await service.transfer(amount);
  res.json(transferResult);
});
