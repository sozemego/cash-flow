const fs = require("fs");
const { getSockets } = require("./socketRegistry");

const playerString = fs.readFileSync("player.json", { encoding: "utf-8" });
const player = JSON.parse(playerString);

function transfer(amount) {
  console.log(
    `Attempting to transfer ${amount} to player ${JSON.stringify(player)}`
  );

  const { cash } = player;

  if (cash < Math.abs(amount)) {
    return { amountTransferred: 0, current: player.cash };
  }

  player.cash += amount;
  savePlayer();
  syncCashChange(amount);
  return { amountTransferred: amount, current: player.cash };
}

function savePlayer() {
  console.log("Saving player");
  fs.writeFileSync("player.json", JSON.stringify(player));
}

function syncCashChange(amount) {
  const sockets = getSockets();
  console.log(`Sync player to ${sockets.length} sockets`);
  sockets.forEach(socket =>
    socket.send(JSON.stringify({ type: "PLAYER_CASH_CHANGED", amount }))
  );
}

module.exports = {
  getPlayer: () => player,
  transfer
};
