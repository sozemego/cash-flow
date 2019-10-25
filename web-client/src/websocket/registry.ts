interface Sockets {
  [url: string]: WebSocket;
}

/**
 * Contains all WebSocket objects that were created.
 */
const sockets: Sockets = {};

/**
 * Creates a WebSocket connected to a given url.
 */
export function createSocket(url: string, dispatch: Function) {
  const socket = new WebSocket(url);
  sockets[url] = socket;
  socket.onmessage = function onMessage(msg) {
    if (dispatch) {
      const payload = JSON.parse(msg.data);
      dispatch(payload);
    }
  };
  return socket;
}

export function isSocketCreated(url: string): boolean {
  return !!getSocket(url);
}

export function getSocket(url: string): WebSocket {
  return sockets[url];
}
