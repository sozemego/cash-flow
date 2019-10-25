import { AppEvent, AppEventType, TruckArrivedEvent } from "./types";

const textTransformers = {
  [AppEventType.TRUCK_ARRIVED]: (event: TruckArrivedEvent): string => {
    return `[truckId=${event.truckId}] arrived!`;
  }
};

export function getText(event: AppEvent): string {
  const transformer = textTransformers[event.type];
  return transformer(event);
}
