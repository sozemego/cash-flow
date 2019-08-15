/**
 * Contains all WebSocket objects that were created.
 */
const sockets = {};

/**
 * Creates a WebSocket connected to a given url.
 */
export function createSocket(url, dispatch) {
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

export function isSocketCreated(url) {
  return !!getSocket(url);
}

export function getSocket(url) {
  return sockets[url];
}
