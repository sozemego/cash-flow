export function getCurrentGameDate(clock) {
  const { multiplier, startTime } = clock;
  const now = Date.now();
  const timePassed = now - startTime;
  const currentTime = startTime + timePassed * multiplier;
  return new Date(currentTime);
}

function format(num) {
  if (num > 9) {
    return "" + num;
  }
  if (num < 10) {
    return "0" + num;
  }
}

export function getFormattedDateTime(date) {
  const hour = date.getHours();
  const minute = date.getMinutes();

  const dayOfMonth = date.getDate();
  const month = date.getMonth() + 1;
  const year = date.getFullYear();

  return `${format(dayOfMonth)}-${format(month)}-${format(year)} ${format(
    hour
  )}:${format(minute)}`;
}

export function getFormattedTime(date) {
  const hour = date.getHours();
  const minute = date.getMinutes();

  return `${format(hour)}:${format(minute)}`;
}
