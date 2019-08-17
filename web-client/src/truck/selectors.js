import {useSelector} from "react-redux";

export function useGetTrucks() {
	const trucks = useSelector(state => {
		return state.truck.trucks;
	});
	return trucks;
}
