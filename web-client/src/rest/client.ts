let token: string | null = null;

export function setToken(_token: string | null) {
  token = _token;
}

export async function get(url: string) {
  const headers: any = {};
  if (token) {
    headers["Authorization"] = `Bearer ${token}`;
  }
  return fetch(url, { headers });
}

export async function getAsJson(url: string) {
  const response = await get(url);
  return response.json();
}
