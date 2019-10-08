import express = require("express");
import { service } from "./service";
import { Request, Response, NextFunction } from "express";

export const router = express.Router();

router.use((req: Request, res: Response, next: NextFunction) => {
  console.log(
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
  console.log(`Returning ${JSON.stringify(player)}`);
  res.send(player);
});

router.post("/transfer", (req: Request, res: Response) => {
  const { query } = req;
  const amount = Number(query.amount);
  console.log(`Call to transfer with ${amount}`);
  if (!amount) {
    res.status(400).end();
    return;
  }

  const transferResult = service.transfer(amount);
  res.json(transferResult);
});
