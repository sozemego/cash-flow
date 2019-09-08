export function makeActionCreator(type, ...fields) {
	const actionCreator = function(...args) {
		const action = {
			type,
		};

		fields.forEach((field, index) => {
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