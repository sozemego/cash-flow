const ws = require("ws");
const {server} =  require("../index");

const Server = ws.Server;

const socketServer = new Server({server, path: '/websocket'});

socketServer.on('connection', ws => {
	console.log('Connected!');
});