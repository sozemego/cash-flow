export function getCurrentGameDate(clock) {
  const { multiplier, startTime } = clock;
  const now = Date.now();
  const timePassed = now - startTime;
  const currentTime = now + timePassed * multiplier;
  return new Date(currentTime);
}
