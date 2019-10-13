export interface Action {
    type: string,
    [index: string]: any
}

export interface HasType {
    type: string
}

export type ActionCreator = ((...args: any[]) => Action) & HasType

export function makeActionCreator(type: string, ...fields: string[]): ActionCreator {
	const actionCreator: ActionCreator = function(...args: any): Action {
		const action: Action = {
			type,
		};

		fields.forEach((field: string, index: number) => {
			action[field] = args[index];
		});

		return action;
	};
	actionCreator.type = type;
	actionCreator.toString = function() {
		return type;
	};
	return actionCreator;
}