Clock
---

Provides functionality related to the game clock.

Clock in the game goes at a different speed than the real clock. One real second will advance time in game by 1 minute.
So the time multiplier is 60 (it is 60 times faster in game than real time).

Other services reliant on this service will not start until this service is available, as their functionality is tied to the clock.
