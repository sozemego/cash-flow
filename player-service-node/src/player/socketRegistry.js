const sockets = [];

function addSocket(socket) {
	console.log('Adding socket');
  sockets.push(socket);
  console.log(`There are currently ${sockets.length} sockets connected.`);
}

function removeSocket(socket) {
	console.log('Disconnecting socket');
  const index = sockets.findIndex(element => element === socket);
  if (index > -1) {
    sockets.splice(index, 1);
  }
	console.log(`There are currently ${sockets.length} sockets connected.`);
}

function getSockets() {
  return sockets;
}

module.exports = {
  addSocket,
  removeSocket,
  getSockets
};
