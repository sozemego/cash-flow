import { IClock } from "./index";

export function getCurrentGameDate(clock: IClock): Date {
  const { multiplier, startTime } = clock;
  const now = Date.now();
  const timePassed = now - startTime;
  const currentTime = startTime + timePassed * multiplier;
  return new Date(currentTime);
}

function format(num: number): string {
  if (num > 9) {
    return "" + num;
  }
  if (num < 10) {
    return "0" + num;
  }
  return "";
}

export function getFormattedDateTime(date: Date): string {
  const hour = date.getHours();
  const minute = date.getMinutes();

  const dayOfMonth = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();

  return `${format(dayOfMonth)}-${format(month)}-${format(year)} ${format(
    hour
  )}:${format(minute)}`;
}

export function getFormattedTime(date: Date): string {
  const hour = date.getHours();
  const minute = date.getMinutes();

  return `${format(hour)}:${format(minute)}`;
}
