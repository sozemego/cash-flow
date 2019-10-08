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

router.get("/player", (req: Request, res: Response) => {
  const player = service.getPlayer();
  logger.info(`Returning ${JSON.stringify(player)}`);
  res.send(player);
});

router.post("/transfer", (req: Request, res: Response) => {
  const { query } = req;
  const amount = Number(query.amount);
  logger.info(`Call to transfer with ${amount}`);
  if (!amount) {
    res.status(400).end();
    return;
  }

  const transferResult = service.transfer(amount);
  res.json(transferResult);
});
