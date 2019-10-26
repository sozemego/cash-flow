import { GameEventParameter, IGameEvent } from "./index";
import { AppState } from "../store";
import { getTrucks } from "../truck/selectors";
import { getCities } from "../world/selectors";

export function getGameEventText(
  gameEvent: IGameEvent,
  state: AppState
): string {
  const { text } = gameEvent;
  const parameters = extractParams(text);
  const filledParameters = findRealTexts(parameters, state);
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
  state: AppState
): GameEventParameter[] {
  return parameters.map(parameter => {
    const { key, value } = parameter;

    return {
      ...parameter,
      text: getText(key, value, state)
    };
  });
}

function getText(key: string, value: string, state: AppState): string {
  const defaultText = `[${key}=${value}]`;
  if (key === "truckId") {
    const trucks = getTrucks(state);
    const truck = trucks[value];
    return truck ? truck.name : defaultText;
  }
  if (key === "cityId") {
    const cities = getCities(state);
    const city = cities[value];
    return city ? city.name : defaultText;
  }
  return defaultText;
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
