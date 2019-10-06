const express = require('express');
const playerRouter = require('./player/route');


const app = express();
const port = 9005;

app.use("/", playerRouter);

app.listen(port, () => console.log('App listening on ' + port));