const  express = require('express');
const router = express.Router();

const { getPlayer, transfer } = require("./service");

router.use((req, res, next) => {
  console.log("Request at " + new Date().toISOString() + " to " + req.path + " " + JSON.stringify(req.query));
  next();
});

router.get("/player", (req, res) => {
  const player = getPlayer();
  console.log(`Returning ${JSON.stringify(player)}`);
  res.send(player);
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
  res.json(result);
});

module.exports = router;
