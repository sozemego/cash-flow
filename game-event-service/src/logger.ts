import winston = require("winston");
import { Logger } from "winston";
import { Format } from "logform";

const consoleTransport = new winston.transports.Console();

const defaultOptions: winston.LoggerOptions = {
  transports: [consoleTransport],
  level: "info",
  format: getDefaultFormat()
};

function getDefaultFormat() {
  const { combine, timestamp, printf } = winston.format;

  return combine(
    timestamp(),
    printf(info => {
      return `${info.timestamp} ${info.level.toUpperCase()}: ${info.message}`;
    })
  );
}

function padRight(str: string, maxLength: number): string {
    const pad = Array.from({ length: maxLength }).join(" ");
    return (str + pad).substr(0, maxLength);
}

function getNamedFormat(name: string): Format {
  const { combine, timestamp, printf } = winston.format;

  return combine(
    timestamp(),
    printf(info => {
      return `${info.timestamp} [${padRight(name, 25)}][${padRight(info.level.toUpperCase(), 4)}] ${
        info.message
      }`;
    })
  );
}

export const logger = winston.createLogger(defaultOptions);

export function namedLogger(name: string): Logger {
  const namedOptions: winston.LoggerOptions = {
    transports: [consoleTransport],
    level: "info",
    format: getNamedFormat(name)
  };

  return winston.createLogger(namedOptions);
}
