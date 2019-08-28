import { useEffect, useState } from "react";
import { createSocket, getSocket, isSocketCreated } from "./registry";

export const READY_STATE_TABLE = {
  [WebSocket.OPEN]: "open",
  [WebSocket.CLOSED]: "closed",
  [WebSocket.CLOSING]: "closing",
  [WebSocket.CONNECTING]: "connecting"
};

/**
 *
 * @param url
 * @param dispatch
 * @returns {{readyState: number socket: WebSocket}}
 */
export function useWebsocket(url, dispatch) {
  const [readyState, setReadyState] = useState(WebSocket.CLOSED);

  useEffect(() => {
    if (!isSocketCreated(url)) {
      const socket = createSocket(url, dispatch);
      socket.onopen = function onOpen() {
        console.log("WebSocket connected to " + url);
        setReadyState(WebSocket.OPEN);
      };
    }
  }, [url, dispatch]);

  const socket = getSocket(url);

  return { socket, readyState };
}
