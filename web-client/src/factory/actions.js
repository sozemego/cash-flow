import { makeActionCreator } from "../store/actionCreator";

export const FACTORY_ADDED = "FACTORY_ADDED";
export const factoryAdded = makeActionCreator(FACTORY_ADDED, 'factory');