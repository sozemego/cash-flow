import { JsonResponse } from "./index";

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

export async function getAsJson<T>(url: string): Promise<JsonResponse<T>> {
  const response: Response = await get(url);
  const json: T = await response.json();
  return {
    payload: json,
    ...response
  };
}


