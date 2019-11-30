const Transport = require("winston-transport");
const axios = require("axios");

export class HttpAppenderTransport extends Transport {
  constructor(opts) {
    super(opts);
  }

  log(log, callback) {
		setImmediate(callback);

    axios
      .post(
        "http://localhost:8999/log-aggregator-service/log",
        this.convert(log)
      )
      .catch(error => console.error(error));
  }

  convert(log) {
    return {
      application: "player-service",
      level: log.level,
      message: log.message
    };
  }
}
