const fs = require("fs");

const playerString = fs.readFileSync("player.json", { encoding: "utf-8" });
const player = JSON.parse(playerString);

function transfer(amount) {
  console.log(`Attempting to transfer ${amount} to player ${JSON.stringify(player)}`);

  if (amount > 0) {
  	player.cash += amount;
  	savePlayer();
    return { amountTransferred: amount, current: player.cash };
  }

  const { cash } = player;
  if (cash < Math.abs(amount)) {
    return { amountTransferred: 0, current: player.cash };
  }

  player.cash += amount;
  savePlayer()
  return { amountTransferred: amount, current: player.cash };
}

function savePlayer() {
	console.log('Saving player');
	fs.writeFileSync('player.json', JSON.stringify(player));
}

module.exports = {
  getPlayer: () => player,
	transfer
};
