const express = require("express");
const router = express.Router();

const { getPlayer, transfer } = require("./service");

router.use((req, res, next) => {
  console.log("Request at " + new Date().toISOString() + " to " + req.path + " " + JSON.stringify(req.query));
  next();
});

router.get("/player", (req, res) => {
  res.send(getPlayer());
});

router.post("/transfer", (req, res) => {
  const { query } = req;
  const amount = Number(query.amount);
  console.log(`Call to transfer with ${amount}`);
  if (!amount) {
    res.status(400).end();
    return;
  }

  const result = transfer(amount);
  console.log(result);
  res.send(result);
});

module.exports = router;