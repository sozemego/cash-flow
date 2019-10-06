const express = require("express");
const router = express.Router();

const { getPlayer } = require("./service");

router.use((req, res, next) => {
  console.log("Request at " + new Date().toISOString() + " to get player");
  next();
});

router.get("/player", (req, res) => {
  res.send(getPlayer());
});

module.exports = router;
