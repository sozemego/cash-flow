import { CITY_ADDED } from "./actions";

const initialState = {
  cities: {}
};

export function reducer(state = initialState, action) {
  switch (action.type) {
    case CITY_ADDED:
      return cityAdded(state, action);
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
