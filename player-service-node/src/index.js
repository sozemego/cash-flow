const express = require('express');
const http = require('http');
const playerRouter = require('./player/route');

const app = express();
const server = http.createServer(app);
const port = 9005;

app.use("/", playerRouter);

server.listen(port, () => console.log('App listening on ' + port));

module.exports = {
	server
};