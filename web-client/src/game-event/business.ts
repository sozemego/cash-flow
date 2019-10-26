import { GameEventParameter, IGameEvent } from "./index";
import { MiddlewareAPI, Dispatch } from "redux";
import { Action, AppState } from "../store";
import { getTrucks } from "../truck/selectors";
import { getCities } from "../world/selectors";

export function getGameEventText(
  gameEvent: IGameEvent,
  api: MiddlewareAPI<Dispatch<Action>, AppState>
): string {
  const { text } = gameEvent;
  const parameters = extractParams(text);
  const filledParameters = findRealTexts(parameters, api.getState);
  return applyParameters(text, filledParameters);
}

function extractParams(text: string): GameEventParameter[] {
  const parameters: GameEventParameter[] = [];

  let startIndex = -1;
  let endIndex = -1;
  for (let i = 0; i < text.length; i++) {
    const char = text[i];
    if (char === "[") {
      startIndex = i;
    }
    if (char === "]") {
      endIndex = i;
    }
    if (startIndex > -1 && endIndex > -1) {
      const subtext = text.substring(startIndex + 1, endIndex);
      const tokens = subtext.split("=");
      const parameter: GameEventParameter = {
        key: tokens[0],
        value: tokens[1],
        startIndex,
        endIndex
      };
      parameters.push(parameter);
      startIndex = -1;
      endIndex = -1;
    }
  }

  return parameters;
}

function findRealTexts(
  parameters: GameEventParameter[],
  getState: () => AppState
): GameEventParameter[] {
  return parameters.map(parameter => {
    const { key, value } = parameter;

    return {
      ...parameter,
      text: getText(key, value, getState)
    };
  });
}

function getText(key: string, value: string, getState: () => AppState): string {
  if (key === "truckId") {
    const trucks = getTrucks(getState());
    const truck = trucks[value];
    return truck.name;
  }
  if (key === "cityId") {
    const cities = getCities(getState());
    const city = cities[value];
    return city.name;
  }
  return `[${key}=${value}]`;
}

function applyParameters(
  originalText: string,
  parameters: GameEventParameter[]
): string {
  let indexOffset = 0;
  let result = originalText;
  parameters.forEach(parameter => {
    const { startIndex, endIndex, text } = parameter;
    const originalLength = endIndex - startIndex;
    const textLength = text!.length;
    const start = result.substring(0, startIndex + indexOffset);
    const end = result.substring(endIndex + 1 + indexOffset);
    result = start + text + end;
    indexOffset = textLength - originalLength - 1;
  });
  return result;
}
