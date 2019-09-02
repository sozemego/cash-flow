import { CITY_ADDED, CITY_HIGHLIGHTED } from "./actions";

const initialState = {
	cities: {},
	highlightedCity: null,
};

export function reducer(state = initialState, action) {
	switch (action.type) {
		case CITY_ADDED:
			return cityAdded(state, action);
		case CITY_HIGHLIGHTED:
			return cityHighlighted(state, action);
		default:
			return state;
	}
}

function cityAdded(state, action) {
	const { city } = action;
	const cities = { ...state.cities };
	cities[city.id] = city;
	return { ...state, cities };
}

function cityHighlighted(state, action) {
	const { cityId } = action;
	return { ...state, highlightedCity: cityId };
}
