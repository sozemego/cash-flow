export function makeActionCreator(type, ...fields) {
	return (...args) => {
		const action = {
			type,
		};

		fields.forEach((field, index) => {
			action[field] = args[index];
		});

		return action;
	}
}