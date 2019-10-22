package com.soze.common;

import java.util.ArrayList;
import java.util.List;

public class CollectionsUtils {

	public static <T> List<T> fromIterable(Iterable<T> iterable) {
		List<T> list = new ArrayList<>();
		iterable.forEach(list::add);
		return list;
	}

}
