import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { FACTORY_SERVICE_URL } from "../config/urls";
import { factoryAdded } from "./actions";

// here we will connect to the factory websocket
/**
 * @type {WebSocket}
 */
let socket = null;

const readyStateTable = {
  [WebSocket.OPEN]: "open",
  [WebSocket.CLOSED]: "closed",
  [WebSocket.CLOSING]: "closing",
  [WebSocket.CONNECTING]: "connecting"
};

export function isConnected() {
  if (socket == null) {
    return false;
  }
  return socket.readyState === WebSocket.OPEN;
}

export function connect(reduxDispatch, update) {
  return new Promise((resolve, reject) => {
    socket = new WebSocket(FACTORY_SERVICE_URL + "/websocket");
    socket.onopen = function onOpen() {
			console.log("socket to factory service is open!");
			update(val => !val);
      return resolve(socket);
    };

    socket.onmessage = function onMessage(msg) {
      console.log("message from factory service");
      const payload = JSON.parse(msg.data);
      if (payload.type == "FACTORY_ADDED") {
        console.log(payload);
        reduxDispatch(factoryAdded(payload.factoryDTO));
      }
    };

    socket.onclose = function onClose() {
			update(val => !val);
			reject();
		};

    socket.onerror = function onError(error) {
    	reject(error);
		};
  });
}

export function useFactorySocket() {
  const reduxDispatch = useDispatch();

  const [update, setUpdate] = useState(false);

	const socketState = socket
		? readyStateTable[socket.readyState]
		: readyStateTable[WebSocket.CLOSED];

  useEffect(() => {
    if (!isConnected() && socketState != readyStateTable[WebSocket.CONNECTING]) {
      connect(
        reduxDispatch,
				setUpdate
      );
    }
  }, [socketState]);

  return { socket, state: socketState };
}
