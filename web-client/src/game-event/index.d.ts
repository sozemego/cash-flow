export interface GameEvent {
    text: string;
}

export interface GameEventProps {
    event: GameEvent
}

export interface GameEventListProps {
    events: GameEvent[]
}