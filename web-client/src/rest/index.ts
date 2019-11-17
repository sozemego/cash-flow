export interface HasJsonPayload<T> {
    payload: T
}

export type JsonResponse<T> = Response & HasJsonPayload<T>